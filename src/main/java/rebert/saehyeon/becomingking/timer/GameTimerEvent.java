package rebert.saehyeon.becomingking.timer;

import me.saehyeon.saehyeonlib.main.SaehyeonLibListener;
import me.saehyeon.saehyeonlib.role.Role;
import me.saehyeon.saehyeonlib.timer.Timer;
import me.saehyeon.saehyeonlib.timer.event.TimerStopEvent;
import rebert.saehyeon.becomingking.BecomingKing;

public class GameTimerEvent implements SaehyeonLibListener {
    void onTimerEnd(TimerStopEvent e) {
        Timer timer = e.getTimer();

        // 게임 종료 -> 현재 왕인 사람이 승리
        BecomingKing.Stop(Role.getByName("king"));
    }
}
