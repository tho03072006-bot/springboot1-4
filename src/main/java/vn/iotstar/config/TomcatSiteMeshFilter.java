package vn.iotstar.config;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.config.properties.PropertiesFilterConfigurator;
import org.sitemesh.config.xml.XmlFilterConfigurator;
import org.sitemesh.webapp.SiteMeshFilter;
import org.sitemesh.webapp.WebAppContext;
import org.sitemesh.webapp.contentfilter.ResponseMetaData;

/**
 * SiteMesh 3.2.1 extension for Tomcat 11: render decorators using include.
 * Keeps XML mappings and reload support. No third-party classes are replaced.
 * See https://github.com/sitemesh/sitemesh3/blob/master/JAKARTA_UPGRADE.md
 */
public class TomcatSiteMeshFilter extends ConfigurableSiteMeshFilter {
    private FilterConfig config;

    @Override public void init(FilterConfig config) throws ServletException {
        this.config = config;
        super.init(config);
    }

    @Override protected Filter setup() throws ServletException {
        SiteMeshFilterBuilder builder = new SiteMeshFilterBuilder() {
            @Override public Filter create() {
                boolean includeErrors = isIncludeErrorPages();
                return new SiteMeshFilter(getSelector(), getContentProcessor(), getDecoratorSelector(), includeErrors) {
                    @Override protected WebAppContext createContext(String type, HttpServletRequest req,
                            HttpServletResponse resp, ResponseMetaData metadata) {
                        return new WebAppContext(type, req, resp, config.getServletContext(), getContentProcessor(), metadata, includeErrors) {
                            @Override protected void dispatch(HttpServletRequest request, HttpServletResponse response, String path)
                                    throws ServletException, IOException {
                                var dispatcher = getServletContext().getRequestDispatcher(path);
                                if (dispatcher == null) throw new ServletException("Decorator not found: " + path);
                                dispatcher.include(request, response);
                            }
                        };
                    }
                };
            }
        };
        new PropertiesFilterConfigurator(getObjectFactory(), getConfigProperties(config)).configureFilter(builder);
        new XmlFilterConfigurator(getObjectFactory(), loadConfigXml(config, getConfigFileName())).configureFilter(builder);
        applyCustomConfiguration(builder);
        return builder.create();
    }
}
