瀑布流的标签选择器
================
****
### yinshuai
### E-mail:1317972280@qq.com
****

## 效果展示
![效果](https://github.com/yinshuaiblog/Lable/blob/master/Lable.gif)

### 属性说明
|属性名|用途|
|----|-----|
|`lable_notextColor`|**未选中时文字的颜色**|
|`lable_opttextColor`|**选中时文字的颜色**|
|`lable_margin`|**外边距(int型)**|
|`lable_padding`|**内边距(int型)**|
|`lable_textSize`|**文字的大小**|
|`lable_ismultiple`|**是否可多选 false-单选  true-多选**|

### 方法说明
|方法名|用途|
|----|-----|
|`setDataList(List<String>)`|**设置标签的数据**|
|`clearSelect()`|**取消全部选中**|
|`setOnItemSelectClickListener()`|**设置标签的选中事件监听**|
|`setOnCancelSelectClickListener()`|**设置标签的取消选中事件监听**|
|`setOnCancelAllSelectListener()`|**设置取消全部选中的事件监听**|
|`getSelectContent()`|**获取选中的文本内容**|
