package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.TrainingReview;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.TrainingReviewPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingReviewRepository extends JpaRepository<TrainingReview, TrainingReviewPK> {

    List<TrainingReview> findByTrainingProgrammeId(Long trainingProgrammeId);
    List<TrainingReview> findByUserId(Long userId);

}
