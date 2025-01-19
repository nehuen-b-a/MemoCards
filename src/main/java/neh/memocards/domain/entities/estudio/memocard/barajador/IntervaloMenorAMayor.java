package neh.memocards.domain.entities.estudio.memocard.barajador;

import lombok.NoArgsConstructor;
import neh.memocards.domain.entities.estudio.memocard.MemoCard;
import utils.CircularList;


import java.util.Comparator;
import java.util.List;

@NoArgsConstructor
public class IntervaloMenorAMayor implements ITipoDeBarajador {

    @Override
    public CircularList<MemoCard> barajarComienzoDeSesion(List<MemoCard> memoCardsNuevas, List<MemoCard> memoCardsRepaso) {

        CircularList<MemoCard> memoCardsBarajadas = new CircularList<>();

        List<MemoCard> repaso = memoCardsRepaso.stream().sorted(Comparator.comparingInt(MemoCard::getIntentos)).toList();

        memoCardsBarajadas.addAll(repaso);
        memoCardsBarajadas.addAll(memoCardsNuevas);

        memoCardsBarajadas.get(memoCardsBarajadas.size() - 1);

        return null;
    }


}
