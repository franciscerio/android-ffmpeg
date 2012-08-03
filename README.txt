===new in this fork
I liked the newer log implementation , esp in ./ffmpeg/cmdutils.c in the 
'guardian' project. But , i also needed the java layer, jni integration, and 
the 'videokit' directory and library  from 'halfninja' version of 'android-ffmpeg'.

Halfninja solved some wierd fprintf vs av_log issue that is no longer 
relevant in the current ffmpeg release. Native logger is fine and , 
you can see it in android logcat by redirecting (stdout, stderr)
to logcat.  That means that jni android stuff can link directly against 
the current version of ffmpeg.c and the current version of cmdutils.c. 

So, i merged  the most useful , jni related stuff from halfninja/ 
into guardian and then added code of mine :

buttons in the main Android activity w/ binding to  following features:

-select a picture
-start a recording ( you talk about the picture
-stop  recording ( sound track persists to a .3gp codec=amrnb
-mux  creates a video using the selected picture and the audio recording 
   mux output (container=3gp, audio=amrnb, video=mpeg4

setup
-----
same NDK
edit  ./Project/jni/settings.sh to conform with your NDK setup, NDK_Base etc. 
edit ./Project/local.properties  setting home dirs for SDK, NDK
edit ./Project/project.properties setting the android api  you will use 
./Project dir android update using below :  exec cmd from project root dir
      $ android update project --target 1 --name RecorderActivity --path Project
 edit & reset the AndroidManifest.xml file to what u are using

building 
-------
same 2 steps for the submodules ( i moved them ,but they need  'init' and 'update' 
    submodules install to ./ffmpeg dir and to ./x264 dir under ./Project/jni
cd Project/jni
./configure_make_everything.sh
$NDK/ndk-build
cd ./Project
./ant debug      
output of above will be the apk file in the ./bin directory
install the apk 
redirect stderr to android logcat so u can see all the stdout/ stderr msgs from ffmpeg
  $ adb shell setprop log.redirect-stdio true
test the apk looking at logcat stmts from ffmpeg ... lots of them

if  u have root ...
adb push the /libs/armv7-a/ffmpeg executable to /data/local/tmp/ffmpeg. 
get a shell on the phone.  run ffmpeg CLI expressions right there.
with CLI,  u may try out lots of possible permutations of the ffmpeg switches
when u get a set of switch values that u like , plug them into the java
file at ./RecorderActivity.java and the java interface will mimic the transform
that you have tested on the CLI in the shell on the phone. 


=== pre fork notes from guardian version
This is a new android-ffmpeg project since it seems there were so many
different ways of doing it, it was confusing.  So here is my clean, easily
changeable, static ffmpeg creator for Android.  The result is a single
'ffmpeg' that is statically linked, so its the only file you need.

setup
-----

 1. Install the Android NDK
 2. On Debian/Ubuntu, run: apt-get install yasm bash patch make


building
--------

cd android-ffmpeg
git submodule init
git submodule update
NDK_BASE=/path/to/android-ndk ./configure_make_everything.sh

That should give you command line binary ffmpeg/ffmpeg, which is the only file
you should need.


customizing
-----------

If you want to change which coders, decoders, muxers, filters, etc that are
included, edit configure_ffmpeg.sh and add/substract what you want.


sources of inspiration
----------------------

https://github.com/mconf/android-ffmpeg
https://github.com/halfninja/android-ffmpeg-x264
https://github.com/guardianproject/SSCVideoProto
http://wiki.multimedia.cx/index.php?title=FFmpeg_filter_howto


testing
-------

./ffmpeg -f lavfi -i "movie=midr-mjpeg.mov,redact=redact_unsort.txt" -f lavfi -i "amovie=midr-mjpeg.mov,aredact=aredact_unsort.txt" -acodec copy -vcodec copy -y /tmp/output.mov

./ffmpeg -f lavfi -i "movie=../testfiles/sscvideoproto_nexuss_high_quality_2_3_3.mp4,redact=redact_unsort.txt" -f lavfi -i "amovie=../testfiles/sscvideoproto_nexuss_high_quality_2_3_3.mp4,aredact=aredact_unsort.txt" -acodec copy -vcodec libx264 -b:v 1000k -an -f mp4 -y /tmp/output.mp4
