package luyao.util.ktx.core.util.log

/**
 * 日志等级
 */
enum class LogLevel {
    /**
     * 错误
     */
    ERROR {
        override val value: Int
            get() = 0
    },
    /**
     * 警告
     */
    WARN {
        override val value: Int
            get() = 1
    },
    /**
     * 信息
     */
    INFO {
        override val value: Int
            get() = 2
    },
    /**
     * 调试
     */
    DEBUG {
        override val value: Int
            get() = 3
    };

    abstract val value: Int
}