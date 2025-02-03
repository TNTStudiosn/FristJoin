package com.TNTStudios.fristjoin.client.mixin;

import com.TNTStudios.fristjoin.client.RulesScreen;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public abstract class RulesScreenKeyMixin {

    /**
     * Intercepta el método keyPressed de todas las pantallas.
     * Si la pantalla actual es una RulesScreen, se cancelan las teclas:
     * ESC (256), E (69), F1 (290), F3 (292) y F5 (294).
     *
     * Las demás teclas, incluidas las flechas (para cambiar de página), se permiten.
     */
    @Inject(method = "keyPressed(III)Z", at = @At("HEAD"), cancellable = true)
    private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        // Convertir "this" a Object para forzar la comprobación en tiempo de ejecución
        if (((Object)this) instanceof RulesScreen) {
            if (keyCode == 256 || keyCode == 69 || keyCode == 290 || keyCode == 292 || keyCode == 294) {
                // Cancelar el evento para que no se procese la tecla
                cir.setReturnValue(true);
            }
        }
    }
}
