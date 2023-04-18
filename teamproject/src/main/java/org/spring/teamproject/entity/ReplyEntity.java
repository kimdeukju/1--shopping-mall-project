package org.spring.teamproject.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "reply_tb")
@AllArgsConstructor
@NoArgsConstructor
public class ReplyEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reply_id", nullable = false)
	public Long id;

	@Column(nullable = false)
	private String re_content;

	@Column(nullable = false)
	private String re_writer;

	@ManyToOne
	private BoardEntity boardId;




}
