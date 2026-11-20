# GroupDocs Redaction Java – Remove Hidden Text and Comments from Word

## Overview
This showcase project demonstrates how to use **GroupDocs Redaction** (Java) to delete comments from a Word document and redact sensitive phrases (including a hidden draft note) before the file is shared externally. It is a minimal, runnable Maven project that helps developers sanitize review markup and classification wording in one pass.

## Prerequisites
- **Java Development Kit (JDK) 8** or higher
- **Apache Maven** 3.5+ installed and added to your `PATH`
- An **Internet connection** for Maven to download dependencies

## License
GroupDocs Redaction requires a license file to work without evaluation limitations (trial badge, one opened document per process, and so on).

1. **Obtain a temporary license** – Visit https://purchase.groupdocs.com/temporary-license/ to get a free 30-day temporary license.
2. **Place the license file** – Save the downloaded `GroupDocs.Redaction.Java.lic` file in the **project root directory** (the same level where `pom.xml` resides).
3. **Without a license** – The library runs in evaluation mode, which adds a trial badge and opens only one document per process. Evaluation mode also applies only the first redaction object, so a license is recommended when you need both comment removal and phrase replacement together.

## Project Structure
```
remove-hidden-text-comments-word-groupdocs-redaction-java/
│   pom.xml
│   README.md
│   GroupDocs.Redaction.Java.lic   (optional – place your license here)
│
├───src
│   └───main
│       └───java
│           └───com
│               └───groupdocs
│                   └───redaction
│                       └───examples
│                           └───RemoveHiddenTextCommentsWordExample.java
│
├───resources
│   ├───input
│   │   └───sample.docx            (sample Word document with comments – replace with your own)
│   └───output                     (the sanitized document will be saved here)
```

## Setup & Run
1. **Clone or copy the project**
   ```bash
   git clone https://github.com/your-repo/remove-hidden-text-comments-word-groupdocs-redaction-java.git
   cd remove-hidden-text-comments-word-groupdocs-redaction-java
   ```
2. **Add your license file** (optional but recommended) – copy `GroupDocs.Redaction.Java.lic` into the project root.
3. **Place a Word document** you want to sanitize inside `resources/input/`. The project already contains `sample.docx`, which you can replace.
4. **Build the project**
   ```bash
   mvn clean compile
   ```
5. **Run the example**
   ```bash
   mvn exec:java
   ```

## What Happens When You Run It?
- The program loads the license (if present).
- It checks `resources/input/sample.docx`. If the file is missing, execution stops with an error message.
- An output directory `resources/output/` is created if it does not already exist.
- `DeleteAnnotationRedaction` removes all comments from the document.
- `ExactPhraseRedaction` replaces `Confidential`, `Internal`, and the hidden draft note `HIDDEN NOTE: vendor pricing from Acme must stay Internal.` with `[REDACTED]`.
- Rasterization is turned off, so the file is saved as `resources/output/sample_redacted.docx`.
- Console output informs you about the progress and the status of each redaction.

## Customization
- **Remove only matching comments** – use `new DeleteAnnotationRedaction("(?im:(use|show|describe))")` instead of the no-argument constructor.
- **Redact other phrases** – change the `ExactPhraseRedaction` arguments, or add another phrase object to the array.
- **Hide a match with a color box** – pass `new ReplacementOptions(java.awt.Color.BLACK)` instead of a replacement string.
- **Sanitize another document** – replace `resources/input/sample.docx`, or change `INPUT_FILE` in the source.

## Notes
- The included `sample.docx` is an Internal Contoso briefing. It contains reviewer comments and the phrases used by the sample, including one run marked as hidden text in Word.
- Evaluation mode applies only the first redaction object (`DeleteAnnotationRedaction`). Add a license when you also need the phrase replacements in the same run.
- `Redactor.save()` with no arguments writes a rasterized PDF. This example disables rasterization so the output keeps the `.docx` extension.
- Ensure the `resources/input/` and `resources/output/` directories are at the same level as `pom.xml` as shown in the structure above.

---

*This is a showcase project for GroupDocs Redaction. It is intended for demonstration and educational purposes.*
