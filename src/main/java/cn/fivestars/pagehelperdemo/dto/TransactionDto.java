package cn.fivestars.pagehelperdemo.dto;

/**
 * @author liangchenzhou1024@163.com
 * @date 2020/12/26
 */
public class TransactionDto {
    private Long id;

    private String name1;

    private String name2;

    public TransactionDto() {
    }

    public TransactionDto(Long id, String name1, String name2) {
        this.id = id;
        this.name1 = name1;
        this.name2 = name2;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    @Override
    public String toString() {
        return "TransactionDto{" +
                "id=" + id +
                ", name1='" + name1 + '\'' +
                ", name2='" + name2 + '\'' +
                '}';
    }
}
