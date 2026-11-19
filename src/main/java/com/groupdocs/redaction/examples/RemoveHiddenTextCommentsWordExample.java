package com.groupdocs.redaction.examples;

import com.groupdocs.redaction.Redactor;
import com.groupdocs.redaction.redactions.DeleteAnnotationRedaction;
import com.groupdocs.redaction.redactions.ExactPhraseRedaction;
import com.groupdocs.redaction.options.ReplacementOptions;
import com.groupdocs.redaction.licensing.License;

import java.io.File;

/**
 * Demonstrates how to remove hidden text and comments from a Word document before sharing it with external parties.
 * <p>
 * This example:
 * <ul>
 *   <li>Applies redaction to delete all annotations (comments)</li>
 *   <li>Replaces exact phrases (e.g., "Confidential", "Internal") with placeholder text</li>
 *   <li>Saves the sanitized document to output</li>
 * </ul>
 * 
 * @see <a href="https://docs.groupdocs.com/redaction/java/">GroupDocs Redaction for Java Documentation</a>
 * @see <a href="https://purchase.groupdocs.com/temporary-license">Temporary License</a>
 */
public class RemoveHiddenTextCommentsWordExample {

    public static void main(String[] args) {
        setupLicense();
        run();
    }

    private static void setupLicense() {
        // Try to apply licensed license first
        String licensePath = "groupdocs.lic";
        File licenseFile = new File(licensePath);
        if (licenseFile.exists()) {
            License license = new License();
            license.setLicense(licensePath);
        }
        // Otherwise, falls back to evaluation mode
    }

    public static void run() {
        // -----------------------------------------------------------------
        // Step 1. Load a Word document from the input folder.
        // -----------------------------------------------------------------
        String inputPath = "src/main/resources/input/sample.docx";
        String outputPath = "src/main/resources/output/sanitized.docx";

        // Ensure output directory exists
        new File("src/main/resources/output").mkdirs();

        try (Redactor redactor = new Redactor(inputPath)) {
            // -----------------------------------------------------------------
            // Step 2. Delete all annotations (comments) from the document.
            // -----------------------------------------------------------------
            redactor.apply(new DeleteAnnotationRedaction());

            // -----------------------------------------------------------------
            // Step 3. Replace exact phrases like "Confidential" and "Internal" with placeholder text.
            // -----------------------------------------------------------------
            redactor.apply(new ExactPhraseRedaction("Confidential", new ReplacementOptions("[REDACTED]")));
            redactor.apply(new ExactPhraseRedaction("Internal", new ReplacementOptions("[REDACTED]")));

            // -----------------------------------------------------------------
            // Step 4. Save the redacted document to the output folder.
            // -----------------------------------------------------------------
            redactor.save(outputPath);

            System.out.println("Sanitized document saved to: " + outputPath);
        }
    }
}
