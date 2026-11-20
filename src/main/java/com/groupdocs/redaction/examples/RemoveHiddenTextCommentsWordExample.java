package com.groupdocs.redaction.examples;

import com.groupdocs.redaction.Redaction;
import com.groupdocs.redaction.RedactionStatus;
import com.groupdocs.redaction.Redactor;
import com.groupdocs.redaction.RedactorChangeLog;
import com.groupdocs.redaction.RedactorLogEntry;
import com.groupdocs.redaction.exceptions.TrialLimitationsException;
import com.groupdocs.redaction.licensing.License;
import com.groupdocs.redaction.options.RasterizationOptions;
import com.groupdocs.redaction.redactions.DeleteAnnotationRedaction;
import com.groupdocs.redaction.redactions.ExactPhraseRedaction;
import com.groupdocs.redaction.redactions.ReplacementOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Demonstrates how to remove comments and redact sensitive phrases from a Word document
 * before sharing it externally, using GroupDocs Redaction.
 * <p>
 * The example expects <code>sample.docx</code> in <code>resources/input/</code> (project root).
 * The sanitized document is saved to <code>resources/output/sample_redacted.docx</code>.
 * {@link DeleteAnnotationRedaction} removes comments; {@link ExactPhraseRedaction} replaces
 * <code>Confidential</code>, <code>Internal</code>, and a hidden draft note in the body.
 * </p>
 */
public class RemoveHiddenTextCommentsWordExample {

    private static final String INPUT_FILE = "resources/input/sample.docx";
    private static final String OUTPUT_DIR = "resources/output/";
    private static final String LICENSE_FILE = "GroupDocs.Redaction.Java.lic";
    private static final String REPLACEMENT = "[REDACTED]";
    private static final String HIDDEN_NOTE = "HIDDEN NOTE: vendor pricing from Acme must stay Internal.";

    /**
     * Loads the GroupDocs Redaction license if the license file exists.
     * <p>
     * To get a temporary license, visit:
     * <a href="https://purchase.groupdocs.com/temporary-license/">https://purchase.groupdocs.com/temporary-license/</a>.
     * Place the downloaded <code>GroupDocs.Redaction.Java.lic</code> file in the project root directory.
     * Without a license the library works in evaluation mode with a trial badge and a limit of one opened document per process.
     * </p>
     *
     * @param licensePath path to the license file relative to the project root
     */
    public static void loadLicense(String licensePath) {
        File licenseFile = new File(licensePath);
        if (licenseFile.exists()) {
            try {
                License license = new License();
                license.setLicense(licensePath);
                System.out.println("GroupDocs Redaction license loaded successfully.");
            } catch (Exception e) {
                System.err.println("Failed to load GroupDocs Redaction license: " + e.getMessage());
            }
        } else {
            System.out.println("License file not found at " + licensePath + ". Running in evaluation mode.");
        }
    }

    /**
     * Deletes all comments, then replaces sensitive body phrases with <code>[REDACTED]</code>.
     * <p>
     * Rasterization is disabled so the output stays a Word document. Evaluation mode applies only
     * the first redaction object in the array, so a license is recommended when you need both
     * comment removal and phrase replacement in one run.
     * </p>
     */
    public static void sanitizeDocument() {
        Path outDirPath = Paths.get(OUTPUT_DIR);
        try {
            if (!Files.exists(outDirPath)) {
                Files.createDirectories(outDirPath);
                System.out.println("Created output directory: " + outDirPath.toAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Failed to create output directory: " + e.getMessage());
            return;
        }

        File inputFile = new File(INPUT_FILE);
        if (!inputFile.exists()) {
            System.err.println("Input file not found: " + INPUT_FILE);
            return;
        }

        // Longer hidden phrase first, then the shorter classification words.
        Redaction[] redactions = new Redaction[] {
                new DeleteAnnotationRedaction(),
                new ExactPhraseRedaction(HIDDEN_NOTE, new ReplacementOptions(REPLACEMENT)),
                new ExactPhraseRedaction("Confidential", new ReplacementOptions(REPLACEMENT)),
                new ExactPhraseRedaction("Internal", new ReplacementOptions(REPLACEMENT))
        };
        String outputFile = outDirPath.resolve("sample_redacted.docx").toString();

        // save() with no arguments writes a rasterized PDF. Disable rasterization to keep DOCX.
        RasterizationOptions rasterOptions = new RasterizationOptions();
        rasterOptions.setEnabled(false);

        try (Redactor redactor = new Redactor(INPUT_FILE)) {
            RedactorChangeLog result = redactor.apply(redactions);
            if (result.getStatus() == RedactionStatus.Failed) {
                System.err.println("Redaction failed for " + INPUT_FILE + ": " + result.getStatus());
                printLog(result);
                return;
            }

            try (FileOutputStream stream = new FileOutputStream(outputFile)) {
                redactor.save(stream, rasterOptions);
            }
            System.out.println("Redacted file saved: " + Paths.get(outputFile).toAbsolutePath()
                    + " (status: " + result.getStatus() + ")");
            printLog(result);
        } catch (TrialLimitationsException e) {
            System.out.println("Evaluation mode allows only one document in a process. "
                    + "Add " + LICENSE_FILE + " to the project root and run the example again.");
        } catch (Exception e) {
            System.err.println("Error processing " + INPUT_FILE + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Prints the status of each redaction from the change log.
     */
    private static void printLog(RedactorChangeLog result) {
        for (RedactorLogEntry logEntry : result.getRedactionLog()) {
            System.out.println(logEntry.getRedaction().getClass().getSimpleName()
                    + ": " + logEntry.getResult().getStatus());
        }
    }

    public static void main(String[] args) {
        loadLicense(LICENSE_FILE);
        sanitizeDocument();
    }
}
