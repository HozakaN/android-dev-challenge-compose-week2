# Timely countdown

<!--- Replace <OWNER> with your Github Username and <REPOSITORY> with the name of your repository. -->
<!--- You can find both of these in the url bar when you open your repository in github. -->
![Workflow result](https://github.com/HozakaN/android-dev-challenge-compose-week2/workflows/Check/badge.svg)


## :scroll: Description
<!--- Describe your app in one or two sentences -->
This app allows to select a duration between 1 second and 99:59:59 thanks to a custom picker
When the user is ready, the countdown is display with path morphing between numbers, like the Timely app used to do


## :bulb: Motivation and Context
<!--- Optionally point readers to interesting parts of your submission. -->
<!--- What are you especially proud of? -->
The picker slides thanks to a custom pointer input Modifier. When the user stops dragging a widget, it smoothly snaps to a number.
The display is based on [Alex Lockwood work](https://www.androiddesignpatterns.com/2016/11/introduction-to-icon-animation-techniques.html) about morphing numbers,
as we used to see in Timely. Transposition to Compose was a lot easier!

## License
```
Copyright 2020 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```