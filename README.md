# Civ War Java

## Origins of the Project & Objectives

[Civilization Wars](https://www.miniclip.com/games/civilization-wars/fr/) is an online strategy game developed in Flash. Crush the armies of your adversaries and conquer the resources of the map; make your empire grow to the top.

This project aims for producing the same kind of game using Java.

### Specifications

![](http://aygix.free.fr/down.php?path=github/Odepax/civ-war/uml-use-cases.png)

The user has to be able to perform the following actions:

#### Attack a building

With a simple _Drag & Drop_ gesture, the user moves its troops from one building to another. Right after that, the half of the troop in garrison in the _source_ building starts its course, heading for the _target_ building. Attacking an ally building reinforces its garrison.

An assault troop can be improved with speed or strength bonuses --- see section about _modules_ below. The strength multiplier is not applied when the target is an ally building.

If the whole garrison of a building is vanquished during an attack, this building is **captured** by the army whose the attacking troop belongs to.

#### Apply a module to a building

By a _Drag & Drop_ action, the user can apply a _module_ to a building of its choice.

Unlike the [original game](https://www.miniclip.com/games/civilization-wars/fr/), _Civ War Java_ doesn't allow the player to cast magic spells against the enemy buildings and troops, nor to improve its own troops between each match, since the program doesn't have any memory of what happens between two matches.

The application of a _module_ is charged --- payment with game specific virtual money --- and can be performed only on an ally building.

A module might affect:

- A building, once only, at the moment of the application;
- An ally assault troop about to leave for an enemy stronghold from this building;
- An enemy assault troop attacking this building.

A module might modify all the properties exposed by the a building or a troop, including:

- Considering an attack troop:
    - The attack power multiplier;
    - The speed multiplier;
- Considering a building:
    - The garrison regeneration rate;
    - The garrison capacity;
    - The unit count of the current garrison.

When a building is captured, all the modules applied on it are lost.

Besides, the program performs some actions periodically:

#### Birth management

In each building, the garrison troop regenerates itself automatically. The regeneration rate is determined independently for each building.

#### Overpopulation management

In overpopulated buildings --- overpopulation is effective when the unit count of the garrison is greater than the building's garrison capacity ---, the garrison dies instead of regenerating itself.

#### Wealth management

Depending in the controlled buildings, an army earns a certain amount of money periodically. This virtual money allows the player to buy modules in order to improve its buildings.

## Implementation

### Further Code's Explanation

When the application starts, an instance of `civwar.Application` is obtained via the Singleton pattern. The role of this application object is just to initialize a `civwar.view.MainWindow` frame, which is the starting point of the UX. This program is GUI-driven.

The `civwar.model.Game` and `civwar.model.GameLoop` classes also use the Singleton pattern.

#### The Model Layer

![](http://aygix.free.fr/down.php?path=github/Odepax/civ-war/uml-model.png)

The `Module` objects are led to modify the properties of some other business objects:

- The `Modify Outgoing Troop` method is called each time an attack troop leaves the building to another location;
- The `Modify Incoming Troop` method is called each time the building the module is applied on has to handle an attack;
- The `Modify Building` method is called right when the module is added to the building.

Thus, `Module` is an abstract class designed to be extended each time a new module is requested. Each so created subclass overrides the `Modify *` methods in order to effectively apply modifications to other objects.

#### The View Layer

![](http://aygix.free.fr/down.php?path=github/Odepax/civ-war/uml-view.png)

The `Battlefield View` has it's own _Drag & Drop_ handler which is responsible for the _Attack a Building_ use case. It also shares a handler, which is created by the `Game View` and responsible for the application of the modules, with the `Shop View`, which injects it to its children.

##### Interfacing with the Model

![](http://aygix.free.fr/down.php?path=github/Odepax/civ-war/uml-model-interfacing.png)

The View defines interfaces that are implemented by the Model entities so they can be drawn on the canvas representing the battlefield.

#### The Controller is Split into 3 Sub-Layers

![](http://aygix.free.fr/down.php?path=github/Odepax/civ-war/uml-controller.png)

Throughout this section, I'll take the example of the _Move Troops to a Building_ use case, in which the end-user just drags and drops from a building to another.

In this application, what can be seen as the _Controller_ in MVC pattern is divided into 3 parts: the event handlers call a middle data adapter, which modifies the Model, in which some checks are performed on the data.

##### Data-Checking from the Model Layer

The first control layer resides in the core of the application: right in the Model. It's also the most business-related: its role is simply to provide an implementation of the use case mentioned above. Look at the method below:

```java
public void sendTroops(Building source, Building target) { // (1)
    AttackTroop troop = source.detachTroopTo(target); // (2)

    if (troop != null) {
        addAttackTroop(troop); // (3)

        setChanged();
    }
}
```

1. Its only purpose is to move troops from a building to another building, hence the `source` and `target` parameters;
2. So what it actually does is simply to get the troop from the _source_ building...
3. ... and put this into the _troop pool_ so it can walk to its _target_ building by itself --- actually propelled by the game loop periodical action --- like a rolling jolly monkey!

##### The Event Handlers from the View Layer

The second control layer is placed at the level of the View. Concretely, it takes the form of several `MouseListener` implementations across the declared sub-types of Swing control.

A _Drag & Drop_ move can be split up into 2 mouse click events: first the mouse is pressed, then it's released, **somewhere else**... And we need to know the coordinates of both points the mouse was pressed and released at, otherwise we'd send an incomplete request to the model.

Now, have a look on this event handler plugged on the `civwar.model.BattlefieldView` object:

```java
addMouseListener(new MouseAdapter() {
    private Point2D source; // (1)

    @Override
    public void mousePressed(MouseEvent e) {
        if (source == null) {
            source = new Point2D.Double(e.getX(), e.getY()); // (1)
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (source != null) {
            Point2D target = new Point2D.Double(e.getX(), e.getY()); // (2)

            gameController.sendTroops(source, target); // (3)
        }

        source = null;
    }
});
```

1. It's first activated when the mouse is pressed; in this case, it just retains the $( x ; y )$ coordinates of the mouse cursor;
2. Then it's activated again during the _Drop_ phase; there, it gets the coordinates of the point was released on...
3. ... As we've got all the data we needed, we can now perform a call to the middle data adapter.

##### The Middle Controller Layer

This is the actual plain old _Controller_ layer. It's only role is to transform a request incoming from the _View_ to a request that the _Model_ would understand. 'Member: the Model works with `Building` objects, whereas the View works with 2D points. Since the Model does not know what that is, the Controller has to find the building that hides behind each point provided by the View.

#### Thread Safety

Thread synchronization is implemented at the level of the Model entities. Besides, the `civwar.model.Game` Singleton object and the Model entities always use `java.util.Vector` as a thread safe implementation of `java.util.List`.

### Screenshots

![](http://aygix.free.fr/down.php?path=github/Odepax/civ-war/screen-1.png)

**Fig 1** - Splash screen tutorial 1:2.

![](http://aygix.free.fr/down.php?path=github/Odepax/civ-war/screen-2.png)

**Fig 2** - Splash screen tutorial 2:2.

![](http://aygix.free.fr/down.php?path=github/Odepax/civ-war/screen-3.png)

**Fig 3** - The game interface at the beginning of the match.

## To Do <!-- [ 27:50 ] spent on this project. -->

- Port to Kotlin;
- Network features;
- Improve graphics;
- Doodads;
- Map from file;
- Map editor.
