package org.example.jpql;

public class MemberDTO {
    private String useranme;
    private int age;

    public MemberDTO(String useranme, int age) {
        this.useranme = useranme;
        this.age = age;
    }

    public String getUseranme() {
        return useranme;
    }

    public void setUseranme(String useranme) {
        this.useranme = useranme;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
