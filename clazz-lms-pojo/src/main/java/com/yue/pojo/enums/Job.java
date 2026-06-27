package com.yue.pojo.enums;

public enum Job {

    ADMIN(0),
    HEAD_TEACHER(1),
    LECTURER(2),
    STUDENT_AFFAIRS_SUPERVISOR(3),
    TEACHING_AND_RESEARCH_SUPERVISOR(4),
    CONSULTANT(5);

    private final Integer code;

    Job(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static Job fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (Job job : Job.values()) {
            if (job.code.equals(code)) {
                return job;
            }
        }
        return null;
    }
}
