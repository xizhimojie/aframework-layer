/**
 * @Project Name :  aframework
 * @Package Name :  web.testbase
 * @Description :  TODO
 * @author :  dell
 * @Creation Date:  2017-08-11 10:29 AM
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
package web.testbase;

import com.core.utility.AfSpringContext;
import com.core.utility.IWebHelper;
import com.sun.tools.internal.ws.processor.util.DirectoryUtil;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

/**
 * @author :  dell
 * @Description :  TODO
 * @Creation Date:  2017-08-11 10:29 AM
 */

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration(locations = {"classpath*:springTest-config.xml"})
@WebAppConfiguration
public class BaseRestfulTest {
    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private IWebHelper webHelper;

    protected MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }


    @Test
    public void adocBuild() throws IOException {
        String appDir = System.getProperty("user.dir");
        String adocPath = appDir + "/src/docs/api/asciidocs/apiList.adoc";
        StringBuilder content = new StringBuilder();
        content.append("include::" + appDir + "/src/docs/api/asciidocs/preview.adoc");

        File apidirs = new File(appDir + "/target/generated-snippets");
        for (File apidir : apidirs.listFiles()) {
            String apiName = apidir.getName();
            content.append("=== " + apiName + "\n\n");
            fileAppend(content, apidir + "/request-headers.adoc", "request-headers 类型说明");
            fileAppend(content, apidir + "/http-request.adoc", "http-request");
            fileAppend(content, apidir + "/request-parameters.adoc", "request-parameters类型说明");
            fileAppend(content, apidir + "/request-body.adoc", "request-body类型说明");
            fileAppend(content, apidir + "/http-response.adoc", "http-response");
            fileAppend(content, apidir + "/response-fields.adoc", "response-fields 类型说明");
        }
        File file = new File(adocPath);
        FileUtils.writeStringToFile(file, content.toString(), "utf-8");
    }

    private void fileAppend(StringBuilder stringBuilder, String path, String title) {
        File file = new File(path);
        if (file.exists()) {
            stringBuilder.append("==== " + title + " \n\n");
            stringBuilder.append("include::" + file + "[]" + "\n\n");
        }
    }


}
