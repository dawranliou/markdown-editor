# markdown-editor

This is a Markdown Editor implemented in ClojureScript.

![alt screenshot](screencast.gif)

## Overview

The project uses ClojureScript, Reagent, and Figwheel.

## Setup

To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    lein clean

To create a production build run:

    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL. 

## Features

- Download markdown

## Roadmap

- [ ] Save markdown offlie - use localStorage or indexedDb
- [ ] Share markdown - DB backend (or put everything in the query parameter?)
- [ ] Collaborate markdown - This one might actually need a server backend to push data

## License

MIT
