import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class mcm_to_png extends PApplet {

int charWidth = 12;
int charHeight = 18;
String[] input;
int currLine, currIndex;
int x, y, charX, charY;
int skipInBack = 40;
public void setup() {
  size(209, 305);
  background(255, 0, 0); //draw red background
  currLine = 1; //skip the first line (contains "MAX7456") 
  currIndex = 0;
  x = 0;
  y = 0;
  charX = 1;//start first character at 1, 1
  charY = 1;
  input = loadStrings("./OSD_Charset.mcm");

  for (int k=0;k<256;k++) { //256 characters in total
    for (int i=0;i<charWidth*charHeight;i++) {
      noStroke();
      fill(getNextValue(), 255);
      rect(x+charX, y+charY, 1, 1); //using rect because for some reason point() is always semi transparant?
      incrementPos();
    }
    for (int v = 0; v < skipInBack; v++) {//each character is followd by some empty space, so skip that
      getNextValue();
    }
  }
  saveFrame("OSD_Charset.png"); //save image and exit
  exit();
}

public void incrementPos() {//increment position for drawing
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
public int getNextValue() {//get the next color value from the .mcm file 
  if (currLine >= input.length) {
    return 0;
  }
  int returnVal=0;
  String inputLine = input[currLine];
  String val = inputLine.substring(currIndex, currIndex+2);
  if (val.equals("10") ) {
    returnVal = 255;
  }
  else if (val.equals("01")) {
    returnVal =  128;
  }
  currIndex+=2;
  if (currIndex >= inputLine.length()) {
    currIndex=0;
    currLine++;
  }
  return returnVal;
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "mcm_to_png" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
