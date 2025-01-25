package neh.memocards.domain.entities.estudio.memocard.barajador;

import neh.memocards.domain.entities.estudio.memocard.MemoCard;
import utils.CircularList;

import java.util.List;

public interface ITipoBarajador {
    List<MemoCard> barajarComienzoDeSesion(List<MemoCard> memoCardsNuevas, List<MemoCard> memoCardsRepaso, List<MemoCard> memoCardsPendientes);
    List<MemoCard> barajar(List<MemoCard> memoCards);
    List<MemoCard> ordenarMemoCards(List<MemoCard> memoCards);
}
