# Modular sample app for Play Framework 2.4.x with full authentication.

This is a basic web app based on Play Framework. It consist full authentication using [PlayAuthenticate](PlayAuthenticate) module.
It's based on a sample app from [PlayAuthenticate](https://github.com/joscha/play-authenticate) and presents exact same capabilities.

I've created this project to offer an alternative to typical MVC model. I'm not a big fan of MVC since over time it gets really big, messy and hard to maintain. Instead of this horror I prefer to go with composition approach. The idea is simple:
- Whole app is build with 'elements'. Small reusable pieces. Each element consist own views, logic, localization files and whatever it requires.
- Elements can be build with other elements.
- We try to reduce visibility of internal composition of elements as much as possibly. That's why we keep all Java files from each element in a single package. We differentiate type and create structure by using prefixes, like: 'View', 'Service', 'Model' etc. In a perfect situation the only classes that are public are: one entry point and its models that holds nothing but simple data and storage info. By having single entry point, we make maintaining element APIs much easier and we effectively hide internal implementation.
- Controllers are only rendering appropriate elements. They are very simple and consist no logic at all.

## How to run it
1. Open terminal
2. Go to 'play-authenticate-modular' folder
3. Run 'activator run'
4. Enjoy

---

Author: Konrad Gadzinowski <kgadzinowski@gmail.com>