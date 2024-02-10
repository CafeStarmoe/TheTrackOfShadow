[简体中文](https://github.com/YuCraft/TheTrackOfShadow/blob/develop/README%20_CN.md)  
# Build 
## Build Release Version 
 
Clone this repository 
 
```
git clone --depth=1 https://github.com/YuCraft/TheTrackOfShadow.git
```
Then build with gradle 

```
./gradlew clean build
```
 
## Build Dev Version 
 
The development version contains the TabooLib SDK for developers, **it is not runnable on server**.
 
```
./gradlew clean taboolibBuildApi -PDeleteCode
```

> The -PDeleteCode parameter use to remove all logic code to reduce the size.

## Postscript  
**THE VERSION IS NOT THE FINAL VERSION**  
**DO NOT RUN THIS PLUGIN ON ANY STABLE SERVER**  
**WE TRY TO COMPLETE IT**  
