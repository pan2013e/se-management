package zju.se.management.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="DoctorInfo")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DoctorInfo {

    @Id
    @Column
    private int id;

    @Column
    private String department;

    @Column
    private String hospital;

    public void setId(int id) { this.id = id; }

}
