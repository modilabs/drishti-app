describe("Entity Relationships", function () {
    it("should identify all entities based on entity relationship", function () {
        var entityRelationshipJSONDefinition = [
            {
                "parent": "ec",
                "child": "mother",
                "field": "wife",
                "kind": "one_to_one",
                "from": "ec.id",
                "to": "mother.ec_id"
            },
            {
                "parent": "mother",
                "child": "child",
                "field": "children",
                "kind": "one_to_many",
                "from": "mother.id",
                "to": "child.mother_id"
            }
        ];
        var expectedEntities = [
            {
                "type": "ec",
                "relations": [
                    {
                        "type": "mother",
                        "kind": "one_to_one",
                        "as": "parent",
                        "from": "ec.id",
                        "to": "mother.ec_id"
                    }
                ],
                "fields": []
            },
            {
                "type": "mother",
                "relations": [
                    {
                        "type": "ec",
                        "kind": "one_to_one",
                        "as": "child",
                        "from": "mother.ec_id",
                        "to": "ec.id"
                    },
                    {
                        "type": "child",
                        "kind": "one_to_many",
                        "as": "parent",
                        "from": "mother.id",
                        "to": "child.mother_id"
                    }
                ],
                "fields": []
            },
            {
                "type": "child",
                "relations": [
                    {
                        "type": "mother",
                        "kind": "many_to_one",
                        "as": "child",
                        "from": "child.mother_id",
                        "to": "mother.id"
                    }
                ],
                "fields": []
            }
        ];

        var entities = new enketo.EntityRelationships(entityRelationshipJSONDefinition)
            .determineEntitiesAndRelations();

        expect(JSON.stringify(entities)).toBe(JSON.stringify(expectedEntities));
    });

    it("should identify all entities based on entity relationship when ", function () {
        var entityRelationshipJSONDefinition = [
            {
                "parent": "ec",
                "child": "mother",
                "field": "wife",
                "kind": "one_to_one",
                "from": "ec.id",
                "to": "mother.ec_id"
            },
            {
                "parent": "ec",
                "child": "father",
                "field": "husband",
                "kind": "one_to_one",
                "from": "ec.id",
                "to": "father.ec_id"
            },
            {
                "parent": "mother",
                "child": "child",
                "field": "children",
                "kind": "one_to_many",
                "from": "mother.id",
                "to": "child.mother_id"
            },
            {
                "parent": "father",
                "child": "child",
                "field": "children",
                "kind": "one_to_many",
                "from": "father.id",
                "to": "child.father_id"
            }
        ];
        var expectedEntity = [
            {
                "type": "ec",
                "relations": [
                    {
                        "type": "mother",
                        "kind": "one_to_one",
                        "as": "parent",
                        "from": "ec.id",
                        "to": "mother.ec_id"
                    },
                    {
                        "type": "father",
                        "kind": "one_to_one",
                        "as": "parent",
                        "from": "ec.id",
                        "to": "father.ec_id"
                    }
                ],
                "fields": []
            },
            {
                "type": "mother",
                "relations": [
                    {
                        "type": "ec",
                        "kind": "one_to_one",
                        "as": "child",
                        "from": "mother.ec_id",
                        "to": "ec.id"
                    },
                    {
                        "type": "child",
                        "kind": "one_to_many",
                        "as": "parent",
                        "from": "mother.id",
                        "to": "child.mother_id"
                    }
                ],
                "fields": []
            },
            {
                "type": "father",
                "relations": [
                    {
                        "type": "ec",
                        "kind": "one_to_one",
                        "as": "child",
                        "from": "father.ec_id",
                        "to": "ec.id"
                    },
                    {
                        "type": "child",
                        "kind": "one_to_many",
                        "as": "parent",
                        "from": "father.id",
                        "to": "child.father_id"
                    }
                ],
                "fields": []
            },
            {
                "type": "child",
                "relations": [
                    {
                        "type": "mother",
                        "kind": "many_to_one",
                        "as": "child",
                        "from": "child.mother_id",
                        "to": "mother.id"
                    },
                    {
                        "type": "father",
                        "kind": "many_to_one",
                        "as": "child",
                        "from": "child.father_id",
                        "to": "father.id"
                    }
                ],
                "fields": []
            }
        ];

        var rel = new enketo.EntityRelationships(entityRelationshipJSONDefinition)
            .determineEntitiesAndRelations();

        expect(JSON.stringify(rel)).toBe(JSON.stringify(expectedEntity));
    });

    it("should return empty entities list when there are no entities", function () {
        var entityRelationshipJSONDefinition = null;
        var expectedEntities = [];

        var entities = new enketo.EntityRelationships(entityRelationshipJSONDefinition)
            .determineEntitiesAndRelations();

        expect(JSON.stringify(entities)).toBe(JSON.stringify(expectedEntities));
    });
});