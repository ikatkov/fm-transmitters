# Map of FM transmitters around bay area
All data comes from https://radio-locator.com

They, sadly, lack ability to put multiple transmitters on a map.

#How to make your own map by scraping radio-locator.com

Note, that radio-locator.com throttle your requests

* Go to radio-locator.com and find all stations of interest. Filter by FM/AM etc. Example
``
https://radio-locator.com/cgi-bin/locate?select=city&city=90210 
``
* Open page source and extract all URLs that looks like **radio-locator.com/info/*** Example: 
`` https://radio-locator.com/info/KMTG-FM?loc=37.21754%2C-121.86155&locn=95120%20%28San%20Jose%2C%20CA%29``
* You might use aws/sed or might just paste the source to https://regexr.com/ and use ``\/info/.*?loc`` as the regex. 
Capture the resulting list (there is a "list" button) and make all URLs looks like ``https://radio-locator.com/info/KMTG-FM`` 
* Paste all URLs into a file
* Mass download all URLs ``get -i <your file>``. You end up with a bunch of files in a folder
* Now run a bunch of regex to extract the data in the following format `` [lat, long, pop-up text, power in watts]``'. 
See a simple extractor in Java [here](Parser.java). You can run it like
```
 javac Parser.java
 java Parser <folder>
```
It attempts to read all the files in the given folder and parse out the data
* Store the JS array data in [data.js](data.js)
* Open the main html file. Typically [index.html](index.html)
