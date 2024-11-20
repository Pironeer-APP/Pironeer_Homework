package prioneer.homework.board.repository;


import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import prioneer.homework.board.domain.Board;
import prioneer.homework.member.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HomeworkRepository {

    private final EntityManager em;

    //신규회원 추가될때 과제 18개 저장
    public void save(Board board){
        em.persist(board);
    }

    public List<Board> findMemberHomework(Member member){
        return em.createQuery("select b from Board b where b.userMember.memberId= :id order by b.id desc", Board.class)
                .setParameter("id",member.getMemberId())
                .getResultList();
    }

    public List<Board> findByMemberId(String memberId) {
        return em.createQuery("select b from Board b where b.userMember.memberId = :id", Board.class)
                .setParameter("id", memberId)
                .getResultList();
    }

    public Optional<Board> findByBoardId(Long boardId) {
        return Optional.ofNullable(em.find(Board.class, boardId));
    }

    // 과제 채점
    public void gradeHomework(Board board,Member adminMember) {
        Board homework = findByBoardId(board.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("과제를 찾을 수 없습니다"));

        homework.setResult(board.getResult());
        homework.setFlag(board.isFlag());
        homework.setAdminMember(adminMember);
        homework.setDeposit(board.getDeposit());


    }
}
