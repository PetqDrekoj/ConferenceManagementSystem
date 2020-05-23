package ro.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.domain.*;
import ro.repository.*;
import ro.utils.Message;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    public static final Logger log = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private MyUserRepository myUserRepository;
    @Autowired
    private PcMemberRepository pcMemberRepository;
    @Autowired
    private ScMemberRepository scMemberRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CChairRepository cChairRepository;
    @Autowired
    private ConferenceRepository conferenceRepository;

    public MemberService() {
    }

    public List<MyUser> getAllMembers() {
        return this.myUserRepository.findAll();
    }

    public List<PcMember> getPcMembers() {
        return this.pcMemberRepository.findAll();
    }

    public List<ScMember> getScMembers() {
        return this.scMemberRepository.findAll();
    }

    public List<Author> getAuthors() {
        return this.authorRepository.findAll();
    }

    public List<CChair> getCChairs() {
        return this.cChairRepository.findAll();
    }

    public List<Conference> getConferences() {
        return this.conferenceRepository.findAll();
    }

    public Message<PcMember> addPcMember(Long conferenceId, Long userId) {
        List<PcMember> pcMembers = this.pcMemberRepository.findAll();
        for (PcMember pcMember : pcMembers)
            if (pcMember.getConference_id().equals(conferenceId) && pcMember.getUser_id().equals(userId))
                return new Message<>(null, "You are already a pcMember at this conference");
        PcMember pcMember = new PcMember(userId,conferenceId);
        this.pcMemberRepository.save(pcMember);
        return new Message<>(pcMember, "");
    }

    public Message<ScMember> addScMember(Long userId) {
        List<ScMember> scMembers = this.scMemberRepository.findAll();
        for (ScMember scMember : scMembers)
            if (scMember.getUser_id().equals(userId))
                return new Message<>(null, "You are already a ScMember!");
        ScMember scMember = new ScMember(userId);
        this.scMemberRepository.save(scMember);
        return new Message<>(scMember, "");
    }

    public MyUser getUserFromUsername(String username) {
        log.trace("getUserFromUsername - method entered");
        System.out.println("USERNAME (USER): " + username);
        List<MyUser> users = this.myUserRepository.findAll();
        for (MyUser user : users) {
            if (user.getUsername().equals(username))
                return user;
        }
        return null;
    }

    //TODO: A. (not used)
    public Long getUserIdFromUsername(String username) {
        log.trace("getUserIdFromUsername - method entered");
        System.out.println("USERNAME (ID): " + username);
        List<Long> ids = this.myUserRepository.findAll().stream()
                .filter(myUser -> myUser.getUsername().equals(username))
                .map(BaseEntity::getId)
                .collect(Collectors.toList());

        if (ids.isEmpty()) {
            return null;
        }
        return ids.get(0);
    }
    
    @Transactional
    public Message<MyUser> updateProfile(String username, String fullname, String email, String affiliation, String webpage) {
        log.trace("updateProfile - method entered, {}, {}, {}, {}, {}", username, fullname, email, affiliation, webpage);
        MyUser user = this.getUserFromUsername(username);
        log.trace("user = {}", user);
        if (user != null) {

            this.myUserRepository.findById(user.getId()).ifPresent(u->{
                u.setEmail(email);
                u.setAffiliation(affiliation);
                u.setWeb_page(webpage);
                u.setFullName(fullname);
            });

            log.trace("Service - updateProfile - finished - {}", user);
            return new Message<MyUser>(user, "");
        }
        return new Message<MyUser>(null, "error");
    }

    public Message<MyUser> login(String username, String password) {
        log.trace("login function - entered");
        List<MyUser> users = this.myUserRepository.findAll();
        boolean username_found = false;
        for (MyUser user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password))
                return new Message<MyUser>(user, "");
            if (user.getUsername().equals(username)) username_found = true;
        }
        if (username_found) return new Message<MyUser>(null, "Password was incorrect");
        else return new Message<MyUser>(null, "There was no account with this username");
    }

    public Message<MyUser> register(String givenUsername, String givenPassword, String givenVerifyPassword, String givenEmail, String givenFullName,
                                    String givenAffiliation, String givenWebPage) {
        log.trace("memberService - register function - entered");
        List<MyUser> users = this.myUserRepository.findAll();
        String errorString = "";
        for (MyUser user : users) {
            if (user.getEmail().equals(givenEmail))
                return new Message<MyUser>(null, "Email is already being used");
            if (user.getUsername().equals(givenUsername))
                return new Message<MyUser>(null, "Username is already being used");
        }
        if (givenUsername.equals(""))
            errorString += "Username field required\n";
        if (givenPassword.equals(""))
            errorString += "Password field required\n";
        if (!givenPassword.equals(givenVerifyPassword))
            errorString += "Passwords do not match\n";
        if (givenEmail.equals(""))
            errorString += "Email field required\n";
        else if (!validateEmail(givenEmail))
            errorString += "Please enter a valid email address\n";
        if (!validateWebsite(givenWebPage))
            errorString += "Please enter a valid web page\n";
        if (!errorString.equals(""))
            return new Message<MyUser>(null, errorString);
        MyUser user = new MyUser(givenUsername, givenPassword, givenEmail, givenFullName, givenAffiliation, givenWebPage);
        this.myUserRepository.save(user);
        return new Message<MyUser>(user, "");
    }

    private boolean validateWebsite(String givenWebPage) {
        String[] givenWebPageCharacters = givenWebPage.split("");
        int countDots = 0;
        for (String currentCharacter : givenWebPageCharacters) {
            if (currentCharacter.equals("."))
                countDots += 1;
        }
        return countDots != 0;
    }

    private boolean validateEmail(String givenEmail) {
        String[] givenEmailCharacters = givenEmail.split("");
        int countAts = 0;
        int countDots = 0;
        int position = 0;
        for (String currentCharacter : givenEmailCharacters) {
            if (currentCharacter.equals("@")) {
                if (position == 0)
                    return false;
                countAts += 1;
            }
            if (currentCharacter.equals(".")) {
                if (countAts == 0 || position == 0)
                    return false;
                if (givenEmailCharacters[position - 1].equals("@"))
                    return false;
                countDots += 1;
            }
            position += 1;
        }
        return countAts == 1 && countDots == 1;
    }

    public CChair addCChair(Long user_id, Long conference_id) {
        return cChairRepository.save(new CChair(user_id, conference_id));
    }

    public CChair addCChair(CChair chair){
        return cChairRepository.save(chair);
    }

    public Author addAuthor(Long user_id, Long conference_id) {
        return authorRepository.save(new Author(user_id, conference_id));
    }

    public CChair getChairFromId(Long id) {
        log.trace("am intrat in getchair from id");
        if (id != null) {
            CChair c = this.cChairRepository.findById(id).orElse(null);
            log.trace("am intrat in getchair {}", c);
            return c;
        }
        return null;
    }

    public MyUser getMemberFromId(Long id) {
        log.trace("am intrat in getmemeber from id");
        if(id!= null) {
            MyUser m = this.myUserRepository.findById(id).orElse(null);
            log.trace("am intrat in getmember {}", m);
            return m;
        }
        return null;
    }


    public Author getAuthorById(Long author_id) {
        return authorRepository.findById(author_id).orElse(null);
    }

    public MyUser addUser(String username, String password, String email, String fullname, String affiliation, String webpage){
        return myUserRepository.save(new MyUser(username,password,email,fullname,affiliation,webpage));
    }
}
