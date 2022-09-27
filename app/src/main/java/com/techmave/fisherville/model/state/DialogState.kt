package com.techmave.fisherville.model.state

data class DialogState(var title: String? = null, var content: String? = null, var status: Status = Status.DIALOG_IDLE) {

    enum class Status {

        DIALOG_IDLE,
        DIALOG_SHOW,
        DIALOG_HIDE
    }

    override fun equals(other: Any?) = other is DialogState && other.title == this.title && other.content == this.content && other.status == this.status

    override fun hashCode(): Int {

        var result = title?.hashCode() ?: 0

        result = 31 * result + (content?.hashCode() ?: 0)
        result = 31 * result + status.hashCode()

        return result
    }
}