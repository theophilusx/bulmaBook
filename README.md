
# Table of Contents

1.  [bulmaBook](#orgd3b6563)
    1.  [Overview](#orga2e4da9)
    2.  [Feedback](#org0444694)
    3.  [Development](#orgd099a81)
    4.  [License](#orgc36530f)


<a id="orgd3b6563"></a>

# bulmaBook

FIXME: Write a one-line description of your library/project.


<a id="orga2e4da9"></a>

## Overview

As part of learning Bulma and to extend my Reagent knowledge, I'm implementing
the example used in the book [Creating Interfaces with Bulma](https://jgthms.com/creating-interfaces-with-bulma-ebook/). The book walks
through the creation of a web application focusing on the use of Bulma to style
and mange the application layout. In this repository, I am attempting to
implement the example useing ClojureScript, Reagent and bulma. In many cases,
this involves defining new Reagent components which implement a working example
from the book. for example, a working `navbar` component, which uses bulma for
styling and Reagent + ClojureScript to make the menus work.


<a id="org0444694"></a>

## Feedback

As I develop the components necessary to implement the book example, I will add
them to my Reagent+Bulma component library [yorick](https://github.com/theophilusx/yorick), which I hope to use for other
projects. Once stable, I hope to publish yorick to clojars. 

I'm very keen for feedback - especially ways to improve the components or more
idiomatic Clojure/ClojureScript use. Pull requests on either repository will be
welcome. 


<a id="orgd099a81"></a>

## Development

To get an interactive development environment run:

    clojure -A:fig:build

This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    rm -rf target/public

To create a production build run:

    rm -rf target/public
    clojure -A:fig:min


<a id="orgc36530f"></a>

## License

Copyright © 2018 Tim Cross

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.

