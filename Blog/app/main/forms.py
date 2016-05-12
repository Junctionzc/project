from flask.ext.wtf import Form
from wtforms import StringField, SubmitField

class NameForm(Form):
    name = StringField('What is your name?', validators = [Required()])
    submit = SubmitField('Submit')