package vn.iotstar;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.*;
import vn.iotstar.repository.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class Springboot14ApplicationTests {
    @LocalServerPort int port;
    @Autowired CategoryRepository categories;
    @Autowired UserRepository users;
    @Autowired PasswordEncoder encoder;
    HttpClient client;
    String base;
    @BeforeEach void setup() throws Exception {
        base="http://localhost:"+port+"/springboot1-4";
        categories.deleteAll();
        users.findAll().stream().filter(u -> !u.getUsername().equals("admin")).forEach(users::delete);
        client=newClient();
    }
    HttpClient newClient() { return HttpClient.newBuilder().cookieHandler(new CookieManager(null,CookiePolicy.ACCEPT_ALL)).followRedirects(HttpClient.Redirect.NEVER).build(); }
    HttpResponse<String> get(String path) throws Exception { return client.send(HttpRequest.newBuilder(URI.create(base+path)).GET().build(),HttpResponse.BodyHandlers.ofString()); }
    String token(String body) {
        var m=Pattern.compile("name=\"_csrf\"[^>]*value=\"([^\"]+)\"").matcher(body);
        assertTrue(m.find(),"CSRF token missing in rendered JSP"); return m.group(1);
    }
    HttpResponse<String> post(String path,Map<String,String> data) throws Exception {
        String encoded=data.entrySet().stream().map(e -> URLEncoder.encode(e.getKey(),StandardCharsets.UTF_8)+"="+URLEncoder.encode(e.getValue(),StandardCharsets.UTF_8)).collect(java.util.stream.Collectors.joining("&"));
        return client.send(HttpRequest.newBuilder(URI.create(base+path)).header("Content-Type","application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.ofString(encoded)).build(),HttpResponse.BodyHandlers.ofString());
    }
    void login(String username,String password) throws Exception {
        var page=get("/login"); assertEquals(200,page.statusCode());
        var result=post("/login",Map.of("username",username,"password",password,"_csrf",token(page.body())));
        assertEquals(302,result.statusCode()); assertFalse(result.headers().firstValue("location").orElse("").contains("error"));
    }
    Map<String,String> categoryData(String name) throws Exception { return new HashMap<>(Map.of("name",name,"description","Mô tả danh mục","imageUrl","","active","true","_csrf",token(get("/admin/categories/new").body()))); }
    Map<String,String> userData(String username) throws Exception { return new HashMap<>(Map.of("username",username,"fullname","Nguyễn Văn An","email",username+"@example.com","phone","0912345678","imageUrl","","password","User@12345","role","USER","enabled","true","_csrf",token(get("/admin/users/new").body()))); }
    AppUser seedUser(String name,AppUser.Role role) {
        var u=new AppUser(); u.setUsername(name); u.setFullname("Test User"); u.setEmail(name+"@example.com"); u.setPasswordHash(encoder.encode("User@12345")); u.setRole(role); return users.saveAndFlush(u);
    }
    @Test void anonymousCannotReadOrWriteAdmin() throws Exception {
        assertEquals(302,get("/admin/categories").statusCode());
        assertEquals(403,post("/admin/categories",Map.of("name","Unauthorized")).statusCode());
        assertEquals(0,categories.count());
    }
    @Test void normalUserDeniedAndAdminRevocationAppliesToExistingSession() throws Exception {
        var u=seedUser("member",AppUser.Role.USER); login("member","User@12345"); assertEquals(403,get("/admin/users").statusCode());
        client=newClient(); u.setRole(AppUser.Role.ADMIN); users.saveAndFlush(u); login("member","User@12345"); assertEquals(200,get("/admin/users").statusCode());
        u=users.findById(u.getId()).orElseThrow(); u.setEnabled(false); users.saveAndFlush(u); assertEquals(403,get("/admin/users").statusCode());
    }
    @Test void allMainJspsRenderAndSiteMeshDecoratesExactlyOnce() throws Exception {
        login("admin","Admin@12345");
        for(String path:List.of("/admin","/admin/categories","/admin/categories/new","/admin/users","/admin/users/new")) {
            var r=get(path); assertEquals(200,r.statusCode(),path+": "+r.body());
            assertEquals(1,r.body().split("data-layout=\"admin\"",-1).length-1,path);
            assertTrue(r.body().contains("bootstrap.min.css"));
        }
    }
    @Test void categoryFullCrudSearchAndPagination() throws Exception {
        login("admin","Admin@12345");
        for(int i=0;i<7;i++) assertEquals(302,post("/admin/categories",categoryData("Danh mục "+i)).statusCode());
        var list=get("/admin/categories?q=Danh&size=5&page=0"); assertEquals(200,list.statusCode()); assertTrue(list.body().contains("Trang 1/2"));
        assertTrue(get("/admin/categories?q=Danh&size=5&page=1").body().contains("Trang 2/2"));
        assertTrue(get("/admin/categories?q=Danh&size=5&page=999").body().contains("Trang 2/2"));
        var c=categories.findByNameIgnoreCase("Danh mục 0").orElseThrow();
        assertEquals(200,get("/admin/categories/"+c.getId()).statusCode());
        assertEquals(200,get("/admin/categories/"+c.getId()+"/edit").statusCode());
        var data=categoryData("Đã sửa"); data.put("version",c.getVersion().toString());
        assertEquals(302,post("/admin/categories/"+c.getId(),data).statusCode());
        assertEquals("Đã sửa",categories.findById(c.getId()).orElseThrow().getName());
        assertEquals(302,post("/admin/categories/"+c.getId()+"/delete",Map.of("_csrf",data.get("_csrf"))).statusCode());
        assertFalse(categories.existsById(c.getId()));
    }
    @Test void validationDuplicatesLiteralWildcardsAndEscaping() throws Exception {
        login("admin","Admin@12345");
        var blank=categoryData("   "); assertEquals(200,post("/admin/categories",blank).statusCode()); assertEquals(0,categories.count());
        post("/admin/categories",categoryData("Unique")); post("/admin/categories",categoryData("unique")); assertEquals(1,categories.count());
        var bad=categoryData("Bad URL"); bad.put("imageUrl","javascript:alert(1)"); post("/admin/categories",bad); assertEquals(1,categories.count());
        var xss=categoryData("<script>alert(1)</script>"); post("/admin/categories",xss);
        var body=get("/admin/categories").body(); assertFalse(body.contains("<script>alert(1)</script>")); assertTrue(body.contains("&lt;script&gt;") || body.contains("&#x3c;script&#x3e;"), body);
        var wildcard=get("/admin/categories?q=%25"); assertTrue(wildcard.body().contains("Chưa có kết quả"), wildcard.statusCode()+": "+wildcard.body());
    }
    @Test void userCrudKeepsPasswordWhenBlankAndSearchesEmail() throws Exception {
        login("admin","Admin@12345");
        assertEquals(302,post("/admin/users",userData("newuser")).statusCode());
        var u=users.findByUsernameIgnoreCase("newuser").orElseThrow(); assertTrue(encoder.matches("User@12345",u.getPasswordHash()));
        String hash=u.getPasswordHash();
        assertTrue(get("/admin/users?q=newuser%40example.com&size=5").body().contains("newuser@example.com"));
        assertEquals(200,get("/admin/users/"+u.getId()).statusCode());
        assertEquals(200,get("/admin/users/"+u.getId()+"/edit").statusCode());
        var data=userData("newuser"); data.put("password",""); data.put("fullname","Tên đã sửa"); data.put("version",u.getVersion().toString());
        assertEquals(302,post("/admin/users/"+u.getId(),data).statusCode());
        u=users.findById(u.getId()).orElseThrow(); assertEquals(hash,u.getPasswordHash()); assertEquals("Tên đã sửa",u.getFullname());
        data.put("password","Changed@123"); data.put("version",u.getVersion().toString()); post("/admin/users/"+u.getId(),data);
        assertTrue(encoder.matches("Changed@123",users.findById(u.getId()).orElseThrow().getPasswordHash()));
        assertEquals(302,post("/admin/users/"+u.getId()+"/delete",Map.of("_csrf",data.get("_csrf"))).statusCode()); assertFalse(users.existsById(u.getId()));
    }
    @Test void invalidUserAndDuplicateEmailDoNotPersistOrLeakPassword() throws Exception {
        login("admin","Admin@12345");
        var data=userData("invalid"); data.put("email","not-an-email"); data.put("password","Secret@12345");
        var r=post("/admin/users",data); assertEquals(200,r.statusCode()); assertFalse(r.body().contains("Secret@12345")); assertEquals(1,users.count());
        data=userData("shortpass"); data.put("password","123"); post("/admin/users",data); assertEquals(1,users.count());
        data=userData("duplicate"); data.put("email","ADMIN@example.com"); post("/admin/users",data); assertEquals(1,users.count());
    }
    @Test void selfProtectionCsrfAndStaleVersions() throws Exception {
        login("admin","Admin@12345"); var admin=users.findByUsernameIgnoreCase("admin").orElseThrow();
        var data=userData("admin"); data.put("email",admin.getEmail()); data.put("version",admin.getVersion().toString()); data.put("password","");
        post("/admin/users/"+admin.getId(),data); assertEquals(AppUser.Role.ADMIN,users.findById(admin.getId()).orElseThrow().getRole());
        post("/admin/users/"+admin.getId()+"/delete",Map.of("_csrf",data.get("_csrf"))); assertTrue(users.existsById(admin.getId()));
        assertEquals(403,post("/admin/users/"+admin.getId()+"/delete",Map.of()).statusCode());
        post("/admin/categories",categoryData("Original")); var c=categories.findByNameIgnoreCase("Original").orElseThrow();
        var edit=categoryData("First"); edit.put("version",c.getVersion().toString()); post("/admin/categories/"+c.getId(),edit);
        edit.put("name","Stale"); post("/admin/categories/"+c.getId(),edit); assertEquals("First",categories.findById(c.getId()).orElseThrow().getName());
        assertEquals(404,get("/admin/categories/999999").statusCode());
    }

    @Test void userPaginationPreservesQueryAndInvalidRoleIsRejected() throws Exception {
        for(int i=0;i<7;i++) seedUser("pageuser"+i,AppUser.Role.USER);
        login("admin","Admin@12345");
        var first=get("/admin/users?q=pageuser&size=5&page=0");
        assertEquals(200,first.statusCode()); assertTrue(first.body().contains("Trang 1/2"));
        assertTrue(first.body().contains("q=pageuser"));
        assertTrue(get("/admin/users?q=pageuser&size=5&page=1").body().contains("Trang 2/2"));
        assertTrue(get("/admin/users?q=pageuser&size=5&page=999").body().contains("Trang 2/2"));
        var data=userData("badrole"); data.put("role","ROOT");
        assertEquals(200,post("/admin/users",data).statusCode());
        assertFalse(users.findByUsernameIgnoreCase("badrole").isPresent());
    }
}
