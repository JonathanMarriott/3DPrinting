# 3DPrinting - Slice Supporter
[![FOSSA Status](https://app.fossa.com/api/projects/git%2Bgithub.com%2Fspe-uob%2F3DPrinting.svg?type=shield)](https://app.fossa.com/projects/git%2Bgithub.com%2Fspe-uob%2F3DPrinting?ref=badge_shield)

A program to analyse models for DLP (Resin) 3D printers after slicing to check for islands and add supports where appropriate.

<b>Requirements</b>

- Up to date Java installation (version 15+ recommended)
- 1GB free disk space

<b>Recommendations</b>
- Modern Processor
- 8GB of memory (allocate this to the JVM with argument -Xmx8g)
- Latest version of PrusaSlicer for slicing to SL1

<b>Usage</b>
- Execute the .jar with the command 
  ```shell
   java -Xmx8g -jar SliceSupporter.jar YOURFILEHERE.sl1
  ```
- Provide your sl1 filename when prompted if you did not provide it on the command line
- Program will analyse the file for unsupported regions in the model
- Supports will be added to the model to resolve issues
- A new supported file will be outputted in the same directory as the program with SUPPORTED appended to the end.