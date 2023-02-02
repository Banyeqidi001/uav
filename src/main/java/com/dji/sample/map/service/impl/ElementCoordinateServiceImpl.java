package com.dji.sample.map.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dji.sample.map.dao.IElementCoordinateMapper;
import com.dji.sample.map.model.dto.ElementCoordinateDTO;
import com.dji.sample.map.model.entity.ElementCoordinateEntity;
import com.dji.sample.map.service.IElementCoordinateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sean
 * @version 0.2
 * @date 2021/11/29
 */
@Service
@Transactional
public class ElementCoordinateServiceImpl implements IElementCoordinateService {

    @Autowired
    private IElementCoordinateMapper mapper;

    @Override
    public List<ElementCoordinateDTO> getCoordinateByElementId(String elementId) {
        return mapper.selectList(
                new LambdaQueryWrapper<ElementCoordinateEntity>()
                        .eq(ElementCoordinateEntity::getElementId, elementId))
                .stream()
                .map(this::entityConvertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean saveCoordinate(List<ElementCoordinateDTO> coordinateList, String elementId) {
        for (ElementCoordinateDTO coordinate : coordinateList) {
            ElementCoordinateEntity entity = this.dtoConvertToEntity(coordinate);
            entity.setElementId(elementId);

            int insert = mapper.insert(entity);
            if (insert <= 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean deleteCoordinateByElementId(String elementId) {
        return mapper.delete(new LambdaUpdateWrapper<ElementCoordinateEntity>()
                .eq(ElementCoordinateEntity::getElementId, elementId)) > 0;
    }

    /**
     * Convert database entity objects into coordinate data transfer object.
     * @param entity
     * @return
     */
    private ElementCoordinateDTO entityConvertToDto(ElementCoordinateEntity entity) {
        ElementCoordinateDTO.ElementCoordinateDTOBuilder builder = ElementCoordinateDTO.builder();
        if (entity == null) {
            return builder.build();
        }

        return builder
                .longitude(entity.getLongitude())
                .latitude(entity.getLatitude())
                .altitude(entity.getAltitude())
                .build();
    }

    /**
     * Convert the received coordinate object into a database entity object.
     * @param coordinate
     * @return
     */
    private ElementCoordinateEntity dtoConvertToEntity(ElementCoordinateDTO coordinate) {
        ElementCoordinateEntity.ElementCoordinateEntityBuilder builder = ElementCoordinateEntity.builder();

        if (coordinate == null) {
            return builder.build();
        }

        return builder
                .altitude(coordinate.getAltitude())
                .latitude(coordinate.getLatitude())
                .longitude(coordinate.getLongitude())
                .build();
    }
}
