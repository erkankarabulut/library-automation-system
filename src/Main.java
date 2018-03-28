import repository.UserInfoRepository;

public class Main {

    public static void main(String[] args) {
        UserInfoRepository userInfoRepository = new UserInfoRepository();
        userInfoRepository.connect();
    }
}
