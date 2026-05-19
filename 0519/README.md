# Traditional Image Processing Cartoon Style App

Pure Java Swing implementation of the PDF challenge in this folder. The app uses traditional image processing only:

- Gaussian blur smoothing
- Sobel edge extraction
- Adaptive thresholding
- Color quantization
- Real-time preview controls
- PNG export
- VS Code compatible project layout

## Run in VS Code

1. Open this folder in VS Code.
2. Run the default build task: `Terminal > Run Build Task`.
3. Start the `Run Cartoon App` launch configuration.

The app automatically opens `image.jpg` when it exists in the project folder.

## Run from terminal

```powershell
javac -d out src/cartoon/*.java src/cartoon/gui/*.java src/cartoon/io/*.java src/cartoon/model/*.java src/cartoon/processing/*.java
java -cp out cartoon.Main
```

## Batch export

```powershell
java -cp out cartoon.Main image.jpg cartoon-output.png 2 6 45
```

Arguments after the output path are optional:

- `blurRadius`: `0` to `8`
- `colorLevels`: `2` to `24`
- `edgeDetail`: `0` to `100`
