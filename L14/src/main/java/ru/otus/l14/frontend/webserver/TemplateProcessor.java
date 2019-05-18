package ru.otus.l14.frontend.webserver;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class TemplateProcessor {
    final private String TMPL_DIR = "/html/";
    private final Configuration configuration;

    public TemplateProcessor() {
        configuration = new Configuration(Configuration.VERSION_2_3_28);
        configuration.setClassForTemplateLoading(this.getClass(), TMPL_DIR);
        configuration.setDefaultEncoding("UTF-8");
    }

    public String getPage(String fileName, Map<String, Object> data) throws IOException {
        try (Writer wr = new StringWriter()) {
            Template template = configuration.getTemplate(fileName);
            template.process(data, wr);
            return wr.toString();
        } catch (TemplateException e) {
            throw new IOException(e);
        }
    }
}
