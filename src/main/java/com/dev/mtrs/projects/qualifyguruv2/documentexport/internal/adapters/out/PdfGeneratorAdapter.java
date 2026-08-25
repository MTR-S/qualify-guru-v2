package com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.adapters.out;

import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.domain.PdfGenerationException;
import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.ports.out.PdfGeneratorPort;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class PdfGeneratorAdapter implements PdfGeneratorPort {

    private final Parser parser;
    private final HtmlRenderer renderer;

    public PdfGeneratorAdapter() {
        MutableDataSet options = new MutableDataSet();
        this.parser = Parser.builder(options).build();
        this.renderer = HtmlRenderer.builder(options).build();
    }

    @Override
    public Path generatePdfFromMarkdown(String markdownContent) {
        try {

            String fullHtml = convertToHtml(markdownContent);

            Path tempFilePath = Files.createTempFile("qualify-guru-", ".pdf");

            return streamPdfGenerationToTheDisk(tempFilePath, fullHtml);

        } catch (Exception e) {
            throw new PdfGenerationException("Failed to generate PDF from Markdown", e);
        }
    }

    private String buildXhtmlDocument(String htmlBody) {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8"/>
                    <style>
                        body { font-family: 'Helvetica', 'Arial', sans-serif; font-size: 11pt; line-height: 1.5; color: #333; margin: 40px; }
                        h1 { color: #2c3e50; border-bottom: 1px solid #eee; padding-bottom: 5px; }
                        h2 { color: #34495e; margin-top: 20px; }
                        p, li { margin-bottom: 10px; }
                        /* FIX: Double the percent sign here so Java ignores it */
                        table { width: 100%%; border-collapse: collapse; margin-top: 15px; }
                        th, td { border: 1px solid #bdc3c7; padding: 8px; text-align: left; }
                        th { background-color: #ecf0f1; }
                    </style>
                </head>
                <body>
                    %s
                </body>
                </html>
                """.formatted(htmlBody);
    }

    private String convertToHtml(String markdownContent) {
        String htmlBody = renderer.render(parser.parse(markdownContent));

        return buildXhtmlDocument(htmlBody);
    }

    private Path streamPdfGenerationToTheDisk(Path tempFilePath, String fullHtml) {

        try (OutputStream os = Files.newOutputStream(tempFilePath)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();

            builder.useFastMode();
            builder.withHtmlContent(fullHtml, null);
            builder.toStream(os);
            builder.run();
        } catch (Exception e) {
            throw new PdfGenerationException("Failed to stream PDF content to disk", e);
        }

        return tempFilePath;
    }
}
