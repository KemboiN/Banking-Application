package Banking.Application.Dto;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserRequest
{
	private String firstName;
	private String lastName;
	private String otherName;
	private String email;
	private String password;
	private String idNumber;
	private String gender;
	private String nationality;
	private String address;
	private String phoneNumber;
	private String alternativePhoneNumber;

	
}
