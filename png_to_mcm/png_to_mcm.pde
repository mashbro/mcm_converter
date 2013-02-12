int charWidth = 12;
int charHeight = 18;
String[] input;
int currLine, currIndex;
int x, y, charX, charY;
int skipInBack = 40;

String[] outputString = new String[16385];
PImage img;

void setup() {
  size(209, 305);
  img = loadImage("./OSD_Charset.png");
  img.loadPixels();
  set(0, 0, img);

  outputString[0] = "MAX7456";
  currLine = 1;
  currIndex = 0;
  x = 0;
  y = 0;
  charX = 1;//start first character at 1, 1
  charY = 1;
  for (int k=0;k<256;k++) { //256 characters in total
    for (int i=0;i<charWidth*charHeight;i++) {
      setNextValue(int(brightness(img.pixels[(y+charY)*img.width+(x+charX)])));
      incrementPos();
    }
    for (int v = 0; v < skipInBack; v++) {//each character is followd by some empty space, so skip that    
      setNextValue(128);
    }
  }
  saveStrings("OSD_Charset.mcm", outputString);
  exit();
}

void incrementPos() {//increment position for drawing
  x++;
  if (x >= charWidth) {//reached end of line for character, move down one line
    x = 0;
    y++;
    if (y >= charHeight) {//end of character height reached, jump to position for next character
      y = 0;
      charX += charWidth+1;
      if (charX+charWidth > 209) {//end of row of characters reached, move down one character row
        charX = 1;
        charY+=charHeight+1;
      }
    }
  }
}
void setNextValue(int colorValue) {//get the next color value from the .mcm file
  if (currLine < outputString.length) {
    if(currIndex == 0){//clear the array item to prevent 'null' text showing up
       outputString[currLine] = ""; 
    }
    if (colorValue == 0) {
      outputString[currLine] += "00";
    }
    else if (colorValue == 128) {
      outputString[currLine] += "01";
    }
    else if (colorValue == 255) {
      outputString[currLine] += "10";
    }
    currIndex+=2;
    if (currIndex >= 8) {
      currIndex=0;
      currLine++;
    }
  }
}

