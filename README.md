# AMapMarker-master
## 概述

>提供一种高德地图自定义marker的解决方案以及改善高德官方点聚合功能。
![](https://github.com/Moosphan/AMapMarker-master/blob/master/AMapMarker-master/effect_picture/amap_marker.gif)

## 说明

- 关于聚合：<https://www.jianshu.com/p/f1642bc608c0>

>官方并没有处理多种聚合点的点击事件的处理，这里我对聚合点的点击事件和视角移动监听事件进行了统一的管理和执行，并增加类型判断，让它们不会产生点击冲突，具体讲解请看上面文章。

- 关于自定义Marker：<https://www.jianshu.com/p/557ff3897b2f>

>Marker展示的样式主要通过BitmapDescriptor来决定，而官方提供了很多方法来获取BitmapDescriptor，大家可以根据实际需要来选用。一般如果自定义选用`BitmapDescriptorFactory.fromBitmap()`方法来获取marker视图，只需要将view转为bitmap即可。同时，根据大家的反馈，自定义的marker加载网络图片有很多问题，这里本人对网络加载图片进行了处理，保证图片正常显示，具体可以参考以上文章。

- 补充

>大家如果有什么想法和建议欢迎在issue区提出来，本项目会后续加入更多地图相关功能，如果觉得本项目对你有帮助，欢迎star～

## QQ交流群：

>601924443

## License

```
Copyright 2018 moosphan

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```


