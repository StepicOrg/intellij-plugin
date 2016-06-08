# intellij-plugin 0.2

To work with plugin you must to create a project of Stepic type.
You need to enter login and password your's Stepic account.
And then input course URL or course number.
After one minute course will be built.

## Features
* **Sing in** — you can input login and password, and then you get OAuth2 token.
* **Who am i** — return `user_name`.
* **Download last submission** — download your latest submission this step from site.
* **Get a step status** — return a step status and status of last submission posted from IDE.
* **Send step** — send this step to platform.


## Install
You can download this plugin from https://drive.google.com/open?id=0B3r_Au4BpPbwa1lMYXBzRkIwNzg or build it yourself.

## Build
1. In order to build this project, you have to setting up an development environment (IntelliJ Platform SDK) as described here: http://www.jetbrains.org/intellij/sdk/docs/basics/getting_started/setting_up_environment.html
2. Clone project, e.g. using `git clone https://github.com/StepicOrg/intellij-plugin.git`. 
3. Deploy the plugin as described here: http://www.jetbrains.org/intellij/sdk/docs/basics/getting_started/deploying_plugin.html
