# intellij-plugin 0.6.4

To work with plugin you must to create a project of Stepic type.
You need to enter login and password your Stepic account.
And then input course URL or course number.
After twenty seconds course will be built.

To send task click the left mouse button on file. In the shortcut menu choose "Send step".
In the same way you can to download last submissions and get the status of step.


## Features
* **Sign in** — you can input login and password, and then you get OAuth2 token.
* **Who am i** — return `user_name`.
* **Download Latest Submission** — download your latest submission this step from site.
* **Check Step Status** — return a step status and status of last submission posted from IDE.
* **Submit Solution** — send this step to platform.

## Install
0. Download and install IntelliJ IDEA 2016 (https://www.jetbrains.com/idea/)
1. Download latest version of the plugin from https://drive.google.com/open?id=0B3r_Au4BpPbweXJhYWpYWTdkZnc (intellij-plugin.zip) or build it yourself from the source code.
2. Open IntelliJ IDEA
3. Menu IntelliJ IDEA > Preferences > Plugins > Install plugin from disk... > intellij-plugin.zip
4. Restart IDEA

##  Usage
1. File > New > Project > Stepic
2. Enter your login/password and a course link (in a form of https://stepic.org/course/some-course-name-ID/ or just ID)

## Build
1. In order to build this project, you have to setting up an development environment (IntelliJ Platform SDK) as described here: http://www.jetbrains.org/intellij/sdk/docs/basics/getting_started/setting_up_environment.html
2. Clone project, e.g. using `git clone https://github.com/StepicOrg/intellij-plugin.git`. 
3. Deploy the plugin as described here: http://www.jetbrains.org/intellij/sdk/docs/basics/getting_started/deploying_plugin.html
