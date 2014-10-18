ShoulderSurfing
===============

Mod for Minecraft that changes the F5 third-person camera into an over-the-shoulder view

This source code is provided _as-is_ and is _no longer maintained by me_.

Nearly all the source is dependent on the Forge API.

Features
========

* Over-the-shoulder camera
* Saved settings
* Hotkeys to move the camera a bit (default I-J-K-L)
* Corrective cross-hair positioning
* Dont show player head if camera gets too close to it

Download
========
Required: Forge universal files for your minecraft version (http://files.minecraftforge.net/)
The Forge files go into your minecraft jar.
Tested with Forge 9.10.0.828

Compatibility
=============
Mods that heavily modify certain base classes may break this mod. Your forge client log will show if the code injection fails or not.

Install
=======
First install Forge universal files from files.minecraftforge.net. Make sure it is an up-to-date version for your version on minecraft.

Download ShoulderSurfing

Open the zip file

The JAR file needs to go into the MODS folder under Minecraft. Make sure to remove any previous versions from the COREMODS and MODS folder. This mod will not work if placed anywhere but the MODS folder!

Issues
======
* Prevent camera from clipping into walls in tight spaces (fix behind-head raytrace)
