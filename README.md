## SmartSwipeRefresh
A smart refresh component can customize your slide refresh animation component.

## Parameter Introduction
```kotlin
/**
 * @param onRefresh: Refreshing behavior of data when sliding down.
 * @param state: The state contains some refresh state info.
 * @param loadingIndicator: Specify the refresh animation component.
 * @param content: Some slidable components need to be included here.
 */
@Composable
fun SmartSwipeRefresh(
    onRefresh: suspend () -> Unit,
    state: SmartSwipeRefreshState = remember { SmartSwipeRefreshState() },
    loadingIndicator: @Composable () -> Unit = { CircularProgressIndicator() },
    content: @Composable () -> Unit
)
```
## :camera_flash: Screenshots

<!-- You can add more screenshots here if you like -->
<img src="/samples/smart_refresh.gif" width="260">

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

