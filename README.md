# TODOCLJS

This project is largely a fork of https://github.com/levand/todomvc . The primary changes strip
down the approach in places for demonstration purposes.

This project is used as a demo project for a talk at TriClojure called "ClojureScript Tooling HowTo".
The slides for that talk are available here: http://bit.ly/triclojure-cljs-howto

The approach taken here is a demonstration of using ClojureScript and Quiescent to build a simple TODO
front-end web application using the TODOMVC design.

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
