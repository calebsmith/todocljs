# TODOCLJS

## Overview

## Setup

To get an interactive development environment run:

    lein figwheel
    
or, using Emacs/CIDER, use `M-x cider-jack-in-clojurescript` and start a Figwheel server via Nrepl

Open your browser to [localhost:3449](http://localhost:3449/).

To clean all compiled files:

    lein clean

To create a production build run:

    lein do clean, cljsbuild once min

Open `resources/public/index.html` in a browser. You will get a minfied/optimized build, but will not
get live reloading, nor a REPL. 
