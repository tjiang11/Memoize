# Iteration 5 Evaluation - Group 19

**Evaluator: [Smith, Scott](mailto:scott@cs.jhu.edu)**

## Progress Comments -- on target for iteration 6?

The login should have been working before now.  You don't have a lot of server code, showing you don't have many server features implemented.

You are also behind on the non-CRUD feature but I like the revised plan you propose.  I would try to add as many parameters to this search as you can, e.g. put a time limit on when it stops checking, notifications when in the wrong place (e.g. you missed a meeting because you are at the wrong spot), etc.

Think about how you are going to test these features as well, and also how you can demo them.  You might want to add an admin interface to warp a users time and/or location, and also have it programmatically executable to support better testing.


## Code Inspection

### non-CRUD feature code inspection

(not there yet)

### Package structure of code and other high-level organization aspects

Generally good on this part.  The problems I am getting seem more related to the frameworks being glitchy.

### Code inspection for bad smells anti-patterns, etc

String baseURL = "http://10.0.2.2:" - better to make this a build parameter in gradle or pop up a dialog at app launch during development mode.

        aa.add(item11) etc - should really be reading this from a file or creating a database with this stuff wired in.
		
You currently have all the UI and underlying action code in the same class on the front-end.  This is violating MVC separation of concerns.  Try to factor out some of the model code.  For example User could have the login responsibility on it.  In general your front-end structure classes are just structures, they are the structs of the data-centric aka God Class anti-pattern.

       Log.e("tag", "noooooo"); - not super helpful

There isn't much besides Django template code on the back end so hard to say anything is wrong there.  Make sure to keep good coding style in mind when adding the non-CRUD feature.



## Build / run / test / deploy

I worked more on the Android Studio config stuff and got the app to run.  But it crashed at the login screen.  I have the server running on localhost but you are hard-wiring that into your client?  I edited the code to point to my local server I am running but I then get

E/tag: com.androidnetworking.error.ANError: java.net.ConnectException: Failed to connect to /127.0.0.1:8000

So, I never got the server to receive/send anything to your client when I ran it.

#### Testing

Make sure to keep adding more tests for the new backend features.

## Github

Going pretty well, lots of commits but a bit light on use of issues.

## Iteration Plan / Docs

Make sure to have code docs for both front and back-end available in your iteration 6 report.

## Overall Comments

You are a bit behind now but are still making progress.

**Grade: 83/100**
