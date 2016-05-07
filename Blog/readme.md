# Flask入门学习笔记
**配合《Flask Web开发：基于Python的Web应用开发实战》学习**

## **chapter 1**
### 虚拟环境virtualenv
1. 安装virtualenv包：`$ sudo apt-get install python-virtualenv`
2. 创建虚拟环境：`$ virtualenv venv`
3. 激活虚拟环境：`$ source venv/bin/activate`
4. 退出虚拟环境：`$ deactivate`

### Flask
1. 安装Flask：`(venv) $ pip install flask`
2. 检查Flask是否安装正确：
```
(venv) $ python
>> import flask
>> 
```
没有看见错误证明Flask安装正确

## **chapter 2**
### Jinja2模板引擎
>为了分离业务逻辑和表现逻辑，一般使用模板，模板是一个包含响应文本的文件，用占位符表示动态部分，渲染就是用真实值替换变量，再返回最终得到的响应字符串，Flask使用Jinja2模板引擎渲染模板。

Jinja2提供很多过滤器来修改变量，**注意：千万别在不可信的值上使用safe过滤器，例如用户在表单中输入的文本**

## **chapter 3**
### Bootstrap
>Bootstrap是Twitter开发的一个开元框架，提供的用户组界面可用于创建整洁且具有吸引力的网页。

1. 安装Bootstrap:`(venv) $ pip install flask-bootstrap`

**模板的继承**

语法：`{% extends "xxx.html" %}`

**模板继承的一个例子**

基模板中定义了如下block：
```
{% block content %}
<div class="container">
    {% block page_content %}{% endblock %}
</div>
{% endblock%}
```
衍生模板继承：
```
{% block page_content %}
<div class="page-header">
    <h1>Not Found</h1>
</div>
{% endblock%}
```
效果等同于：
```
{% block content %}
<div class="container">
    <div class="page-header">
        <h1>Not Found</h1>
    </div>
</div>
{% endblock%}
```

**链接**

`url_for()`函数可以利用视图函数名生成URL信息，例如：
>在当前版本的`hello.py`程序中调用`url_for('index')`得到的结果是`/`。调用`url_for('index', _external=True)`返回的则是绝对地址，在这个示例中是`http://localhost:5000/` 。

**静态文件**

对静态文件的引用会被当成一个特殊路由，不需要编写相应的视图函数，例如：
>调用`url_for('static', filename='css/styles.css', _external=True)`得到的结果是`http://localhost:5000/static/css/styles.css` 。

>默认设置下，Flask在程序根目录中名为`static`的子目录中寻找静态文件。如果需要，可在`static`文件夹中使用子文件夹存放文件。服务器收到前面那个URL后，会生成一个响应，包含文件系统中`static/css/styles.css`文件的内容。

>**注意：要使用super()保留基模板中定义的块的原始内容（虽然还不太理解，但被这个坑了一下，没有super()样式就全乱了）**

**使用Flask-Moment本地化日期和时间**

为了在服务器使用统一的时间，而用户浏览器看到的是本地化的时间，利用开源库`moment.js`在浏览器中渲染日期和日期。
1. 安装Flask-Moment扩展:`(venv) $ pip install flask-moment`
