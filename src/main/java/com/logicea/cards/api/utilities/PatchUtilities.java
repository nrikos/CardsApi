package com.logicea.cards.api.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.logicea.cards.api.dtos.CardDTO;
import com.logicea.cards.api.exceptions.CardsApiException;
import com.logicea.cards.api.payloads.CardsApiError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class PatchUtilities {

    private static final List<String> PATH_FIELD_NAMES = Arrays.asList("path", "from", "to");

    public static List<String> extractPathsFromPatchOperations(List<JsonPatchOperation> operations, ObjectMapper deserializer) {
        return operations.stream().flatMap(op -> {
            try {
                Map<String, Object> map = deserializer.readValue(deserializer.writeValueAsString(op), new TypeReference<HashMap<String, Object>>() {});
                return PATH_FIELD_NAMES.stream().map(field -> {
                    if (map.containsKey(field)) {
                        return (String) map.get(field);
                    }
                    return null;
                }).filter(Objects::nonNull);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Invalid set of patch operations!");
            }
        }).collect(Collectors.toList());
    }

    public static void validatePatch(List<JsonPatchOperation> patchOperations,ObjectMapper objectMapper, Class clazz) throws CardsApiException {
        List<String> pathList = PatchUtilities.extractPathsFromPatchOperations(patchOperations, objectMapper);
        Set<String>  invalidPatchAttributes = PatchUtilities.validatePatchPaths(pathList,clazz);
        if(!invalidPatchAttributes.isEmpty())
            throw new CardsApiException(CardsApiError.VALIDATION_ERROR,HttpStatus.BAD_REQUEST,String.format("%s are not valid card attributes", invalidPatchAttributes));
        List<String> invalid = PatchUtilities.isAnyMatch(pathList,CardDTO.getNonPatchAbleAttributes());
        if (!invalid.isEmpty())
            throw new CardsApiException(CardsApiError.VALIDATION_ERROR, HttpStatus.BAD_REQUEST, String.format("%s attribute(s) cannot be updated", invalid));
    }
    private static Set<String> validatePatchPaths(List<String> paths,Class clazz){
        List<String> declaredFields = Arrays.stream(clazz.getDeclaredFields()).map(Field::getName).toList();
        return paths.stream().filter(path->! declaredFields.contains(path.replace("/",""))).collect(Collectors.toSet());
    }

    private static List<String> isAnyMatch(List<String> targetList,List<String> restricted) {
        List<String> matched = new ArrayList<>();
        for (String check : restricted) {
            if (targetList.stream().anyMatch(update -> StringUtils.startsWithIgnoreCase(update, check))) {
                matched.add(check);
            }
        }
        return matched;
    }

    /*
        This could be generic to support different Objects.
     */
    public static  <T> T patchCard(CardDTO entity, ObjectMapper mapper, List<JsonPatchOperation> patchOperations, Class<T> type) throws CardsApiException {
        JsonNode userJsonNode = mapper.convertValue(entity, JsonNode.class);
        try {
            JsonPatch patchRef = new JsonPatch(patchOperations);
            JsonNode patchedNode = patchRef.apply(userJsonNode);
            return mapper.treeToValue(patchedNode,type);

        } catch (JsonPatchException | JsonProcessingException e) {
            throw new CardsApiException(CardsApiError.VALIDATION_ERROR, HttpStatus.BAD_REQUEST, "Invalid update request");
        }
    }
}
