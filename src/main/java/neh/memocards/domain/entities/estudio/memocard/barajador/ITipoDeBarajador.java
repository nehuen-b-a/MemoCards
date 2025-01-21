package neh.memocards.domain.entities.estudio.memocard.barajador;

import neh.memocards.domain.entities.estudio.memocard.MemoCard;
import utils.CircularList;

import java.util.List;

public interface ITipoDeBarajador {
    CircularList<MemoCard> barajarComienzoDeSesion(List<MemoCard> memoCardsNuevas, List<MemoCard> memoCardsRepaso);
    CircularList<MemoCard> barajar(CircularList<MemoCard> memoCards);
}
