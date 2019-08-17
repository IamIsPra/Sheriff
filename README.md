![Sheriff Icon](https://raw.githubusercontent.com/IamIsPra/Sheriff/master/assets/icon.png) 
# Sheriff
Helps You to Grantt Runtime Permission on Android Marshmallow and Above

## How to build

### Gradle

Add Jitpack.io to your project level build.gradle file 
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
  
Add the dependency
```gradle
dependencies {
	  implementation 'com.github.IamIsPra:Sheriff:$sheriffVersion'
	}
```
The Latest Sheriff Version is : [![](https://jitpack.io/v/IamIsPra/Sheriff.svg)](https://jitpack.io/#IamIsPra/Sheriff) 

### Maven

```maven
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>
```
Add the dependency
```maven
<dependency>
	<groupId>com.github.IamIsPra</groupId>
	<artifactId>Sheriff</artifactId>
	<version>1.0.0</version>
</dependency>
```
 
## How to use
  
Create a `Sheriff` object
  
```java
       Sheriff sheriffPermission = Sheriff.Builder()
                .with(this)
                .requestCode(REQUEST_SINGLE_PERMISSION)
                .setPermissionResultCallback(this)
                .askFor(SheriffPermission.CONTACTS)
                .build();
 ```
 and when you want to ask for the permission just call
 ```java
sheriffPermission.requestPermissions();
 ```
 
Override `onPermissionsGranted` and `onPermissionsDenied` methods

Also override 

```java
@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        sheriffPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
```  

