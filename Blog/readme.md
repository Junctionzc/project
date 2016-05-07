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

>**注意：要使用super()保留基模板中定义的块的原始内容（虽然还不太理解，但被这个坑了一下，没有super()样式就全乱了）。**

**使用Flask-Moment本地化日期和时间**

为了在服务器使用统一的时间，而用户浏览器看到的是本地化的时间，利用开源库`moment.js`在浏览器中渲染日期和日期，Flask-Moment是封装`moment.js`的一个Flask扩展。

1. 安装Flask-Moment扩展:`(venv) $ pip install flask-moment`

## **chapter 4**
### Web表单
>生成表单的HTML代码和验证提交表单的数据是一些很单调的任务，Flask-WTF扩展可以简化这些过程。

1. 安装Flask-WTF扩展:`(venv) $ pip install flask-wtf`
2. 设置密匙以防止跨站请求伪造的攻击:`app.config['SECRET_KEY'] = 'hard to guess string'`

**表单类**

>使用Flask-WTF时，每个Web表单都由一个继承自Form的类表示。这个类定义表单中的一组字段，每个字段都用对象表示。字段对象可附属一个或多个验证函数。验证函数用来验证用户提交的输入值是否符合要求。

>例如`StringField('What is your name?', validators=[Required()])`,validators指定一个由验证函数组成的**列表**，在接受用户提交的数据之前验证数据。

>Form基类由Flask-WTF扩展定义，从flask.ext.wtf中导入；字段和验证函数直接从WTForms导入：
```
from flask.ext.wtf import Form
from wtforms import StringField, SubmitField
from wtforms.validators import Required
```

**把表单渲染成HTML**

>当浏览器请求页面时，显示表单的一种方法是利用视图函数把一个`NameForm`类实例通过参数form传入模板，在模板中生成一个简单的表单，但是这种方式太过繁琐。尽量使用Bootstrap中的表单样式，利用Flask-Bootstrap提供的一个非常高端的辅助函数渲染整个Flask-WTF表单：
```
{% import "bootstrap/wtf.html" as wtf %}
{{ wtf.quick_form(fomr) }}
```

**在视图函数中处理表单**

* 提交表单通常使用`post`的方式。
* `NameForm`类实例的`validate_on_submit()`方法在所有验证函数通过时返回True。

**重定向和用户会话**

刷新页面时浏览器会重新发送之前已经发送的最后一个请求，如果这是一个post请求，刷新页面就会重新post，可以使用Post/重定向/Get模式这种技巧。但是这种方式的缺点时会丢失上一次post的数据，可以使用通过session在请求之间”记住“数据。

**Flash消息**

在模板中取flash消息:`{% for message in get_flashed_messages() %}`，使用循环的原因是可能有多个消息在排队等待显示，这个应该是获取消息队列中的下一个消息。

`get_flashed_messages()`函数刷新之后不会再次返回，所以刷新之后flash消息会消失。书上面的这两句话没有因果关系，搞得我看了半天:
>在模板中使用循环是因为在之前的请求循环中每次调用`flash()`函数时都会生成一个消息，所以可能有多个消息在排队等待显示。`get_flashed_messages()`函数获取的消息在下次调用时不会再次返回，因此Flash消息只显示一次，然后就消失了。
