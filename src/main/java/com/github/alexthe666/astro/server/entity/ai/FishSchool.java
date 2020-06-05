package com.github.alexthe666.astro.server.entity.ai;

import com.github.alexthe666.astro.server.entity.AbstractSpaceFish;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;
import static java.lang.Math.pow;

public class FishSchool {
    private static final Random r = new Random();
    private static final int size = 3;
    final double maxForce, maxSpeed;
    Vec3d location, velocity, acceleration;
    private Vec3d migrate = new Vec3d(0.02, 0, 0);
    private int entityID;
    private AbstractSpaceFish fishObj;
    private boolean included = true;

    public FishSchool(AbstractSpaceFish fish) {
        acceleration = new Vec3d(0, 0, 0);
        migrate = new Vec3d(r.nextFloat() - 0.5F, 0, r.nextFloat() - 0.5F);
        velocity = new Vec3d(r.nextFloat() - 0.5F, 0, r.nextFloat() - 0.5F);
        location = fish.getPositionVec();
        maxSpeed = 0.05F;
        maxForce = 0.05F;
        entityID = fish.getEntityId();
        fishObj = fish;
    }

    static double mag(Vec3d vector) {
        return sqrt(pow(vector.x, 2) + pow(vector.z, 2)  + pow(vector.y, 2));
    }

    static double angleBetween(Vec3d v, Vec3d v2) {
        return acos(v.dotProduct(v2) / (mag(v) * mag(v2)));
    }

    void updateIndividual() {
        if (fishObj != null) {
            location = fishObj.getPositionVec();
            velocity = velocity.add(acceleration);
           // velocity = limit(maxSpeed, velocity);
            fishObj.velocity = velocity;
            location = location.add(velocity);
            fishObj.flightTarget = location;
            entityID = fishObj.getEntityId();
        }
        System.out.println(fishObj.flightTarget);
        acceleration = acceleration.mul(0, 0, 0);
        if (r.nextFloat() < 0.1F || fishObj.getPosY() < 0 || fishObj.getPosY() > 256) {
            acceleration = new Vec3d(r.nextFloat() - 0.5F, r.nextFloat() - 0.5F, r.nextFloat() - 0.5F);
            migrate = new Vec3d(r.nextFloat() - 0.5F, r.nextFloat() - 0.5F, r.nextFloat() - 0.5F);
        }
    }

    Vec3d limit(double lim, Vec3d vector) {
        double mag = mag(vector);
        if (mag != 0 && mag > lim) {
            return vector.mul(lim / mag, lim / mag, lim / mag);
        }
        return vector;
    }

    void applyForce(Vec3d force) {
        acceleration = acceleration.add(force);
    }

    Vec3d seek(Vec3d target) {
        Vec3d steer = target.subtract(location);
        steer.normalize();
        steer.mul(maxSpeed, maxSpeed, maxSpeed);
        steer = steer.subtract(velocity);
        steer = limit(maxForce, steer);
        return steer;
    }

    void flock(List<AbstractSpaceFish> boids) {
        view(boids);

        Vec3d rule1 = separation(boids);
        Vec3d rule2 = alignment(boids);
        Vec3d rule3 = cohesion(boids);
        rule1 = rule1.mul(2.5F, 2.5F, 2.5F);
        rule2 = rule2.mul(1.5F, 1.5F, 1.5F);
        rule3 = rule3.mul(1.3F, 1.3F, 1.3F);

        applyForce(rule1);
        applyForce(rule2);
        applyForce(rule3);

        applyForce(migrate);
    }

    void view(List<AbstractSpaceFish> boids) {
        double sightDistance = 100;
        double peripheryAngle = PI * 0.85;

        for (AbstractSpaceFish b : boids) {
            b.included = false;

            if (b.getEntityId() == this.entityID)
                continue;

            double d = location.distanceTo(b.flightTarget);
            if (d <= 0 || d > sightDistance)
                continue;

            Vec3d lineOfSight = b.flightTarget.subtract(location);

            double angle = angleBetween(lineOfSight, velocity);
            if (angle < peripheryAngle)
                b.included = true;
        }
    }

    Vec3d separation(List<AbstractSpaceFish> boids) {
        double desiredSeparation = 50;

        Vec3d steer = new Vec3d(0, 0, 0);
        int count = 0;
        for (AbstractSpaceFish b : boids) {
            if (!b.included)
                continue;

            double d = location.distanceTo(b.flightTarget);
            if ((d > 0) && (d < desiredSeparation)) {
                Vec3d diff = location.subtract(b.flightTarget);
                diff.normalize();
                diff = diff.mul(1D / d, 1D / d, 1D / d);
                steer = steer.add(diff);
                count++;
            }
        }
        if (count > 0) {
            steer = steer.mul(1F / count, 1F / count, 1F / count);
        }

        if (mag(steer) > 0) {
            steer = steer.normalize();
            steer = steer.mul(maxSpeed, maxSpeed, maxSpeed);
            steer = steer.subtract(velocity);
            steer = limit(maxForce, steer);
            return steer;
        }
        return new Vec3d(0, 0, 0);
    }

    Vec3d alignment(List<AbstractSpaceFish> boids) {
        double preferredDist = 50;

        Vec3d steer = new Vec3d(0, 0, 0);
        int count = 0;

        for (AbstractSpaceFish b : boids) {
            if (!b.included)
                continue;

            double d = location.distanceTo(b.flightTarget);
            if ((d > 0) && (d < preferredDist)) {
                steer = steer.add(b.velocity);
                count++;
            }
        }

        if (count > 0) {
            steer = steer.mul(1F / count, 1F / count, 1F / count);
            steer.normalize();
            steer = steer.mul(maxSpeed, maxSpeed, maxSpeed);
            steer = steer.subtract(velocity);
            steer = limit(maxForce, steer);
        }
        return steer;
    }

    Vec3d cohesion(List<AbstractSpaceFish> boids) {
        double preferredDist = 50;

        Vec3d target = new Vec3d(0, 0, 0);
        int count = 0;

        for (AbstractSpaceFish b : boids) {
            if (!b.included)
                continue;

            double d = location.distanceTo(b.flightTarget);
            if ((d > 0) && (d < preferredDist)) {
                target.add(b.flightTarget);
                count++;
            }
        }
        if (count > 0) {
            target = target.mul(1F / count, 1F / count, 1F / count);
            return seek(target);
        }
        return target;
    }


    public void run(List<AbstractSpaceFish> boids) {
        flock(boids);
        updateIndividual();
    }
}
