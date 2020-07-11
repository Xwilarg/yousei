package com.xwilarg.yousei

import com.google.gson.Gson
import kotlin.random.Random

class KanjiLearning : ILearning {

    constructor(content: String) {
        kanjis = Gson().fromJson(content, Array<KanjiInfo>::class.java)
    }

    override fun getQuestion() : Pair<String, String> {
        currentKanji = kanjis[Random.nextInt(0, kanjis.size)]
        return Pair(currentKanji.kanji, if (currentKanji.kunyomi.isEmpty()) {
            currentKanji.onyomi?.get(0)
        } else {
            currentKanji.kunyomi?.get(0)
        })
    }

    override fun checkAnswer(myAnswer: String) : Pair<IsCorrect, String> {
        var isCorrect = false
        var closestAnswer : String? = null
        if (!myAnswer.isNullOrBlank()) {
            for (m in currentKanji.meaning) {
                if (myAnswer == m) {
                    return Pair(IsCorrect.YES, m)
                }
                if (closestAnswer == null && (myAnswer.contains(m) || m.contains(myAnswer))) {
                    closestAnswer = m
                }
            }
        }
        if (closestAnswer != null) {
            return Pair(IsCorrect.PARTIAL, closestAnswer)
        }
        return Pair(IsCorrect.NO, currentKanji.meaning[0])
    }

    override fun getCurrent(): String {
        return currentKanji.kanji
    }

    override fun getRandomChoices(): ArrayList<String> {
        var choices = arrayListOf<String>()
        choices.add(currentKanji.meaning[0])
        while (choices.size < 4) {
            var randomChoice = kanjis[Random.nextInt(0, kanjis.size)].meaning[0]
            if (!choices.contains(randomChoice)) {
                choices.add(randomChoice)
            }
        }
        choices.shuffle()
        return choices
    }

    lateinit var kanjis: Array<KanjiInfo>
    lateinit var currentKanji: KanjiInfo
}