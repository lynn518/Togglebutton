# Togglebutton
Android自定义控件之Togglebutton
### 效果如下

![image](http://raw.github.com/lynn518/ToggleButton/master/screenshots/ToggleButton.gif)

### 添加监听
```
ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               // do something
        });
```
