package com.emu.apps.qcm.services.jpa.entity.tags;

import com.emu.apps.qcm.services.jpa.entity.questions.Question;


public class QuestionTagBuilder {

    private Question question;

    private Tag tag;

    public QuestionTagBuilder setQuestion(Question question) {
        this.question = question;
        return this;
    }

    public QuestionTagBuilder setTag(Tag tag) {
        this.tag = tag;
        return this;
    }

    public QuestionTag build() {
        return new QuestionTag(question, tag);
    }
}